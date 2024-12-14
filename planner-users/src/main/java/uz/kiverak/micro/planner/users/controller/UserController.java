package uz.kiverak.micro.planner.users.controller;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.kiverak.micro.planner.plannerentity.entity.User;
import uz.kiverak.micro.planner.users.mq.func.MessageFuncActions;
import uz.kiverak.micro.planner.users.search.UserSearchValues;
import uz.kiverak.micro.planner.users.service.UserService;
import uz.kiverak.micro.planner.utils.rest.webclient.UserWebclientBuilder;

import java.text.ParseException;
import java.util.NoSuchElementException;
import java.util.Optional;

@RestController
@RequestMapping("/user") // базовый URI
public class UserController {

    public static final String ID_COLUMN = "id";
    public static final Integer DEFAULT_PAGE_SIZE = 10;
    private final UserService userService;
    private final UserWebclientBuilder userWebclientBuilder;
//    private final MessageProducer messageProducer;
    private final MessageFuncActions messageFuncActions;

    public UserController(UserService userService, UserWebclientBuilder userWebclientBuilder, MessageFuncActions messageFuncActions) {
        this.userService = userService;
        this.userWebclientBuilder = userWebclientBuilder;
        this.messageFuncActions = messageFuncActions;
    }

    @PostMapping("/add")
    public ResponseEntity<User> add(@RequestBody User user) {

        if (user.getId() != null && user.getId() != 0) {
            return new ResponseEntity("redundant param: id MUST be null", HttpStatus.NOT_ACCEPTABLE);
        }

        if (user.getEmail() == null || user.getEmail().trim().length() == 0) {
            return new ResponseEntity("missed param: email", HttpStatus.NOT_ACCEPTABLE);
        }

        if (user.getPassword() == null || user.getPassword().trim().length() == 0) {
            return new ResponseEntity("missed param: password", HttpStatus.NOT_ACCEPTABLE);
        }

        if (user.getUsername() == null || user.getUsername().trim().length() == 0) {
            return new ResponseEntity("missed param: username", HttpStatus.NOT_ACCEPTABLE);
        }

        user = userService.add(user);

//        if (user != null) {
//            userWebclientBuilder.initUserData(user.getId()).subscribe(result -> {
//                System.out.println("user populated: " + result);
//            });
//        }

        // send with rabbit with annotations way
//        if (user != null) {
//            messageProducer.initUserData(user.getId());
//        }

        // send with rabbit with functional way
        if (user!= null) {
            messageFuncActions.sendNewUserMessage(user.getId());
        }

        return ResponseEntity.ok(user);
    }

    @PutMapping("/update")
    public ResponseEntity<User> update(@RequestBody User user) {

        if (user.getId() == null || user.getId() == 0) {
            return new ResponseEntity("missed param: id", HttpStatus.NOT_ACCEPTABLE);
        }

        if (user.getEmail() == null || user.getEmail().trim().length() == 0) {
            return new ResponseEntity("missed param: email", HttpStatus.NOT_ACCEPTABLE);
        }

        if (user.getPassword() == null || user.getPassword().trim().length() == 0) {
            return new ResponseEntity("missed param: password", HttpStatus.NOT_ACCEPTABLE);
        }

        if (user.getUsername() == null || user.getUsername().trim().length() == 0) {
            return new ResponseEntity("missed param: username", HttpStatus.NOT_ACCEPTABLE);
        }

        userService.update(user);

        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("/deletebyid")
    public ResponseEntity deleteByUserId(@RequestBody String userId) {
        try {
            userService.deleteByUserId(userId);
        } catch (EmptyResultDataAccessException e) {
            e.printStackTrace();
            return new ResponseEntity("userId=" + userId + " not found", HttpStatus.NOT_ACCEPTABLE);
        }

        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("/deletebyemail")
    public ResponseEntity deleteByUserEmail(@RequestBody String email) {
        try {
            userService.deleteByUserEmail(email);
        } catch (EmptyResultDataAccessException e) {
            e.printStackTrace();
            return new ResponseEntity("email=" + email + " not found", HttpStatus.NOT_ACCEPTABLE);
        }

        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("/id")
    public ResponseEntity<User> findById(@RequestBody Long id) {
        Optional<User> userOptional = userService.findById(id);

        try {
            if (userOptional.isPresent()) {
                return ResponseEntity.ok(userOptional.get());
            }
        } catch (NoSuchElementException e) {
            e.printStackTrace();
        }

        return new ResponseEntity("id=" + id + " not found", HttpStatus.NO_CONTENT);
    }

    @PostMapping("/email")
    public ResponseEntity<User> findByEmail(@RequestBody String email) {
        Optional<User> userOptional = userService.findByEmail(email);

        try {
            if (userOptional.isPresent()) {
                return ResponseEntity.ok(userOptional.get());
            }
        } catch (NoSuchElementException e) {
            e.printStackTrace();
        }

        return new ResponseEntity("email=" + email + " not found", HttpStatus.NOT_ACCEPTABLE);
    }

    @PostMapping("/search")
    public ResponseEntity<Page<User>> search(@RequestBody UserSearchValues userSearchValues) throws ParseException {
        String email = userSearchValues.getEmail() != null ? userSearchValues.getEmail() : null;

        String username = userSearchValues.getUsername() != null ? userSearchValues.getUsername() : null;

//        // проверка на обязательные параметры - если они нужны по задаче
//        if (email == null || email.trim().length() == 0) {
//            return new ResponseEntity("missed param: user email", HttpStatus.NOT_ACCEPTABLE);
//        }

        String sortColumn = userSearchValues.getSortColumn() != null ? userSearchValues.getSortColumn() : null;
        String sortDirection = userSearchValues.getSortDirection() != null ? userSearchValues.getSortDirection() : null;

        Integer pageNumber = userSearchValues.getPageNumber() != null ? userSearchValues.getPageNumber() : 0;
        Integer pageSize = userSearchValues.getPageSize() != null ? userSearchValues.getPageSize() : DEFAULT_PAGE_SIZE;

        // направление сортировки
        Sort.Direction direction = sortDirection == null || sortDirection.trim().length() == 0 || sortDirection.trim().equals("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;

        /* Вторым полем для сортировки добавляем id, чтобы всегда сохранялся строгий порядок.
            Например, если у 2-х задач одинаковое значение приоритета и мы сортируем по этому полю.
            Порядок следования этих 2-х записей после выполнения запроса может каждый раз меняться, т.к. не указано второе поле сортировки.
            Поэтому и используем ID - тогда все записи с одинаковым значением приоритета будут следовать в одном порядке по ID.
         */

        Sort sort = Sort.by(direction, sortColumn, ID_COLUMN);
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize, sort);
        Page<User> result = userService.findByParams(email, username, pageRequest);

        return ResponseEntity.ok(result);
    }

}
