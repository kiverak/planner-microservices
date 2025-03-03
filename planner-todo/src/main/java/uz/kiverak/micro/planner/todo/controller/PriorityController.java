package uz.kiverak.micro.planner.todo.controller;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import uz.kiverak.micro.planner.plannerentity.entity.Priority;
import uz.kiverak.micro.planner.todo.search.PrioritySearchValues;
import uz.kiverak.micro.planner.todo.service.PriorityService;
import uz.kiverak.micro.planner.utils.rest.webclient.UserWebclientBuilder;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/priority")
public class PriorityController {

    private PriorityService priorityService;
    private UserWebclientBuilder userWebclientBuilder;

    public PriorityController(PriorityService priorityService, UserWebclientBuilder userWebclientBuilder) {
        this.priorityService = priorityService;
        this.userWebclientBuilder = userWebclientBuilder;
    }

    @PostMapping("/all")
    public List<Priority> findAll(@RequestBody String userId) {
        return priorityService.findAll(userId);
    }

    @PostMapping("/add")
    public ResponseEntity<Priority> add(@RequestBody Priority priority, @AuthenticationPrincipal Jwt jwt) {

        priority.setUserId(jwt.getSubject());

        if (priority.getId() != null && priority.getId() != 0) {
            return new ResponseEntity("redundant param: id MUST be null", HttpStatus.NOT_ACCEPTABLE);
        }

        if (priority.getTitle() == null || priority.getTitle().trim().length() == 0) {
            return new ResponseEntity("missed param: title", HttpStatus.NOT_ACCEPTABLE);
        }

        if (priority.getColor() == null || priority.getColor().trim().length() == 0) {
            return new ResponseEntity("missed param: color", HttpStatus.NOT_ACCEPTABLE);
        }

//        if (userWebclientBuilder.userExists(priority.getUserId())) {
//            return ResponseEntity.ok(priorityService.add(priority));
//        }

        if (priority.getUserId() != null && !priority.getUserId().isBlank()) {
            return ResponseEntity.ok(priorityService.add(priority));
        }

        return new ResponseEntity("user id=" + priority.getUserId() + " not found", HttpStatus.NOT_FOUND);
    }

    @PutMapping("/update")
    public ResponseEntity update(@RequestBody Priority priority) {
        if (priority.getId() == null || priority.getId() == 0) {
            return new ResponseEntity("missed param: id", HttpStatus.NOT_ACCEPTABLE);
        }

        if (priority.getTitle() == null || priority.getTitle().trim().length() == 0) {
            return new ResponseEntity("missed param: title", HttpStatus.NOT_ACCEPTABLE);
        }

        if (priority.getColor() == null || priority.getColor().trim().length() == 0) {
            return new ResponseEntity("missed param: color", HttpStatus.NOT_ACCEPTABLE);
        }

        priorityService.update(priority);

        return new ResponseEntity(HttpStatus.OK);

    }

    @PostMapping("/id")
    public ResponseEntity<Priority> findById(@RequestBody Long id) {

        Priority priority;
        try {
            priority = priorityService.findById(id);
        } catch (NoSuchElementException e) {
            e.printStackTrace();
            return new ResponseEntity("id=" + id + " not found", HttpStatus.NOT_ACCEPTABLE);
        }

        return ResponseEntity.ok(priority);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity delete(@PathVariable("id") Long id) {
        try {
            priorityService.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            e.printStackTrace();
            return new ResponseEntity("id=" + id + " not found", HttpStatus.NOT_ACCEPTABLE);
        }

        return new ResponseEntity(HttpStatus.OK); // просто отправляем статус 200 (операция прошла успешно)
    }

    @PostMapping("/search")
    public ResponseEntity<List<Priority>> search(@RequestBody PrioritySearchValues prioritySearchValues) {

        // проверка на обязательные параметры
        if (prioritySearchValues.getUserId() == null || prioritySearchValues.getUserId().isBlank()) {
            return new ResponseEntity("missed param: userId", HttpStatus.NOT_ACCEPTABLE);
        }

        return ResponseEntity.ok(priorityService.find(prioritySearchValues.getTitle(), prioritySearchValues.getUserId()));
    }

}
