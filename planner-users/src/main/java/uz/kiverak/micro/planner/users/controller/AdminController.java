package uz.kiverak.micro.planner.users.controller;

import javax.ws.rs.core.Response;

import lombok.extern.log4j.Log4j2;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.kiverak.micro.planner.users.dto.UserDto;
import uz.kiverak.micro.planner.users.keycloak.KeycloakUtils;
import uz.kiverak.micro.planner.users.mq.func.MessageFuncActions;
import uz.kiverak.micro.planner.users.search.UserSearchValues;
import uz.kiverak.micro.planner.utils.rest.webclient.UserWebclientBuilder;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Log4j2
@RestController
@RequestMapping("admin/user") // базовый URI
public class AdminController {

    public static final String ID_COLUMN = "id";
    public static final Integer DEFAULT_PAGE_SIZE = 10;
    public static final int CONFLICT = 409;
    public static final String USER_ROLE_NAME = "user";

//    private final UserService userService;
    private final UserWebclientBuilder userWebclientBuilder;
    //    private final MessageProducer messageProducer;
    private final MessageFuncActions messageFuncActions;
    private final KeycloakUtils keycloakUtils;

    public AdminController(UserWebclientBuilder userWebclientBuilder, MessageFuncActions messageFuncActions, KeycloakUtils keycloakUtils) {
        this.userWebclientBuilder = userWebclientBuilder;
        this.messageFuncActions = messageFuncActions;
        this.keycloakUtils = keycloakUtils;
    }

    @PostMapping("/add")
    public ResponseEntity add(@RequestBody UserDto userDto) {

//        if (userDto.getId() != null && userDto.getId() != 0) {
//            return new ResponseEntity("redundant param: id MUST be null", HttpStatus.NOT_ACCEPTABLE);
//        }

        if (userDto.getEmail() == null || userDto.getEmail().trim().length() == 0) {
            return new ResponseEntity("missed param: email", HttpStatus.NOT_ACCEPTABLE);
        }

        if (userDto.getPassword() == null || userDto.getPassword().trim().length() == 0) {
            return new ResponseEntity("missed param: password", HttpStatus.NOT_ACCEPTABLE);
        }

        if (userDto.getUsername() == null || userDto.getUsername().trim().length() == 0) {
            return new ResponseEntity("missed param: username", HttpStatus.NOT_ACCEPTABLE);
        }

//        userDto = userService.add(userDto);

//        if (userDto != null) {
//            userWebclientBuilder.initUserData(userDto.getId()).subscribe(result -> {
//                System.out.println("userDto populated: " + result);
//            });
//        }

        // send with rabbit with annotations way
//        if (userDto != null) {
//            messageProducer.initUserData(userDto.getId());
//        }

        // send with rabbit with functional way
//        if (userDto!= null) {
//            messageFuncActions.sendNewUserMessage(userDto.getId());
//        }
//
//        return ResponseEntity.ok(userDto);

        Response response = keycloakUtils.createKeycloakUser(userDto);

        if (response.getStatus() == CONFLICT) {
            return new ResponseEntity("user or email already exists: " + userDto.getEmail(), HttpStatus.CONFLICT);
        }

        String userId = CreatedResponseUtil.getCreatedId(response);
        log.info("User created with userId: {}", userId);

        List<String> defaultRoles = new ArrayList<>();
        defaultRoles.add(USER_ROLE_NAME);
        keycloakUtils.addRoles(userId, defaultRoles);

        return ResponseEntity.status(response.getStatus()).build();
    }

//    @PutMapping("/update")
//    public ResponseEntity<User> update(@RequestBody User user) {
//
//        if (user.getId() == null || user.getId() == 0) {
//            return new ResponseEntity("missed param: id", HttpStatus.NOT_ACCEPTABLE);
//        }
//
//        if (user.getEmail() == null || user.getEmail().trim().length() == 0) {
//            return new ResponseEntity("missed param: email", HttpStatus.NOT_ACCEPTABLE);
//        }
//
//        if (user.getPassword() == null || user.getPassword().trim().length() == 0) {
//            return new ResponseEntity("missed param: password", HttpStatus.NOT_ACCEPTABLE);
//        }
//
//        if (user.getUsername() == null || user.getUsername().trim().length() == 0) {
//            return new ResponseEntity("missed param: username", HttpStatus.NOT_ACCEPTABLE);
//        }
//
//        userService.update(user);
//
//        return new ResponseEntity(HttpStatus.OK);
//    }

    @PutMapping("/update")
    public ResponseEntity<UserRepresentation> update(@RequestBody UserDto userDto) {
        if (userDto == null || userDto.getId().isBlank()) {
            return new ResponseEntity("missed param: id", HttpStatus.NOT_ACCEPTABLE);
        }

        keycloakUtils.updateKeycloakUser(userDto);

        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("/deletebyid")
    public ResponseEntity deleteByUserId(@RequestBody String userId) {
//        try {
//            userService.deleteByUserId(userId);
//        } catch (EmptyResultDataAccessException e) {
//            e.printStackTrace();
//            return new ResponseEntity("userId=" + userId + " not found", HttpStatus.NOT_ACCEPTABLE);
//        }

        keycloakUtils.deleteKeycloakUser(userId);

        return new ResponseEntity(HttpStatus.OK);
    }

//    @PostMapping("/deletebyemail")
//    public ResponseEntity deleteByUserEmail(@RequestBody String email) {
//        try {
//            userService.deleteByUserEmail(email);
//        } catch (EmptyResultDataAccessException e) {
//            e.printStackTrace();
//            return new ResponseEntity("email=" + email + " not found", HttpStatus.NOT_ACCEPTABLE);
//        }
//
//        return new ResponseEntity(HttpStatus.OK);
//    }

    @PostMapping("/id")
    public ResponseEntity<UserRepresentation> findById(@RequestBody String userId) {
//    public ResponseEntity<User> findById(@RequestBody String userId) {
//        Optional<User> userOptional = userService.findById(userId);
//
//        try {
//            if (userOptional.isPresent()) {
//                return ResponseEntity.ok(userOptional.get());
//            }
//        } catch (NoSuchElementException e) {
//            e.printStackTrace();
//        }
//
//        return new ResponseEntity("userId=" + userId + " not found", HttpStatus.NO_CONTENT);

        return ResponseEntity.ok(keycloakUtils.findKeycloakUserByUserId(userId));
    }

//    @PostMapping("/email")
//    public ResponseEntity<User> findByEmail(@RequestBody String email) {
//        Optional<User> userOptional = userService.findByEmail(email);
//
//        try {
//            if (userOptional.isPresent()) {
//                return ResponseEntity.ok(userOptional.get());
//            }
//        } catch (NoSuchElementException e) {
//            e.printStackTrace();
//        }
//
//        return new ResponseEntity("email=" + email + " not found", HttpStatus.NOT_ACCEPTABLE);
//    }
//
//    @Deprecated
//    @PostMapping("/search")
//    public ResponseEntity<Page<User>> search(@RequestBody UserSearchValues userSearchValues) throws ParseException {
//        String email = userSearchValues.getEmail() != null ? userSearchValues.getEmail() : null;
//
//        String username = userSearchValues.getUsername() != null ? userSearchValues.getUsername() : null;
//
////        // проверка на обязательные параметры - если они нужны по задаче
////        if (email == null || email.trim().length() == 0) {
////            return new ResponseEntity("missed param: user email", HttpStatus.NOT_ACCEPTABLE);
////        }
//
//        String sortColumn = userSearchValues.getSortColumn() != null ? userSearchValues.getSortColumn() : null;
//        String sortDirection = userSearchValues.getSortDirection() != null ? userSearchValues.getSortDirection() : null;
//
//        Integer pageNumber = userSearchValues.getPageNumber() != null ? userSearchValues.getPageNumber() : 0;
//        Integer pageSize = userSearchValues.getPageSize() != null ? userSearchValues.getPageSize() : DEFAULT_PAGE_SIZE;
//
//        // направление сортировки
//        Sort.Direction direction = sortDirection == null || sortDirection.trim().length() == 0 || sortDirection.trim().equals("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
//
//        /* Вторым полем для сортировки добавляем id, чтобы всегда сохранялся строгий порядок.
//            Например, если у 2-х задач одинаковое значение приоритета и мы сортируем по этому полю.
//            Порядок следования этих 2-х записей после выполнения запроса может каждый раз меняться, т.к. не указано второе поле сортировки.
//            Поэтому и используем ID - тогда все записи с одинаковым значением приоритета будут следовать в одном порядке по ID.
//         */
//
//        Sort sort = Sort.by(direction, sortColumn, ID_COLUMN);
//        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize, sort);
//        Page<User> result = userService.findByParams(email, username, pageRequest);
//
//        return ResponseEntity.ok(result);
//    }

    @PostMapping("/searchByEmail")
    public ResponseEntity<List<UserRepresentation>> searchByEmail(@RequestBody String email) {

        return ResponseEntity.ok(keycloakUtils.searchKeycloakUsersByEmail(email));
    }

}