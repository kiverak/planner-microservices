package uz.kiverak.micro.planner.todo.controller;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import uz.kiverak.micro.planner.plannerentity.entity.Category;
import uz.kiverak.micro.planner.plannerentity.entity.User;
import uz.kiverak.micro.planner.todo.feign.UserFeignClient;
import uz.kiverak.micro.planner.todo.search.CategorySearchValues;
import uz.kiverak.micro.planner.todo.service.CategoryService;
import uz.kiverak.micro.planner.utils.rest.webclient.UserWebclientBuilder;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/category")
public class CategoryController {

    private CategoryService categoryService;
    private UserWebclientBuilder userWebclientBuilder;
    private UserFeignClient userFeignClient;

    public CategoryController(CategoryService categoryService, UserWebclientBuilder userWebclientBuilder, UserFeignClient userFeignClient) {
        this.categoryService = categoryService;
        this.userWebclientBuilder = userWebclientBuilder;
        this.userFeignClient = userFeignClient;
    }

    @PostMapping("/all")
    public List<Category> findAll(@RequestBody String userId) {
        return categoryService.findAll(userId);
    }


    @PostMapping("/add")
    public ResponseEntity<Category> add(@RequestBody Category category, @AuthenticationPrincipal Jwt jwt) {

        category.setUserId(jwt.getSubject());


        if (category.getId() != null && category.getId() != 0) {
            return new ResponseEntity("redundant param: id MUST be null", HttpStatus.NOT_ACCEPTABLE);
        }

        if (category.getTitle() == null || category.getTitle().trim().length() == 0) {
            return new ResponseEntity("missed param: title MUST be not null", HttpStatus.NOT_ACCEPTABLE);
        }

        // sync
//        if (userWebclientBuilder.userExists(category.getUserId())) {
//            return ResponseEntity.ok(categoryService.add(category));
//        }

        // async
//        userWebclientBuilder.userExistsAsync(category.getUserId()).subscribe(user -> System.out.println("user = " + user));

        // feign
//        ResponseEntity<User> result = userFeignClient.findUserById(category.getUserId());
//        if (result == null) {
//            return new ResponseEntity("System unavailable, try later", HttpStatus.NOT_FOUND);
//        }
//
//        if (result.getBody() != null) {
//            return ResponseEntity.ok(categoryService.add(category));
//        }

        if (!category.getUserId().isBlank()) {
            return ResponseEntity.ok(categoryService.add(category));
        }

        return new ResponseEntity("user id=" + category.getUserId() + " not found", HttpStatus.NOT_FOUND);
    }

    @PutMapping("/update")
    public ResponseEntity update(@RequestBody Category category) {

        if (category.getId() == null || category.getId() == 0) {
            return new ResponseEntity("missed param: id", HttpStatus.NOT_ACCEPTABLE);
        }

        if (category.getTitle() == null || category.getTitle().trim().length() == 0) {
            return new ResponseEntity("missed param: title", HttpStatus.NOT_ACCEPTABLE);
        }

        categoryService.update(category);

        return new ResponseEntity(HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity delete(@PathVariable("id") Long id) {

        try {
            categoryService.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            e.printStackTrace();
            return new ResponseEntity("id=" + id + " not found", HttpStatus.NOT_ACCEPTABLE);
        }

        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("/search")
    public ResponseEntity<List<Category>> search(@RequestBody CategorySearchValues categorySearchValues, @AuthenticationPrincipal Jwt jwt) {

        categorySearchValues.setUserId(jwt.getSubject());

        if (categorySearchValues.getUserId() == null || categorySearchValues.getUserId().isBlank()) {
            return new ResponseEntity("missed param: userId", HttpStatus.NOT_ACCEPTABLE);
        }

        List<Category> list = categoryService.findByTitle(categorySearchValues.getTitle(), categorySearchValues.getUserId());

        return ResponseEntity.ok(list);
    }


    @PostMapping("/id")
    public ResponseEntity<Category> findById(@RequestBody Long id) {

        Category category;

        try {
            category = categoryService.findById(id);
        } catch (NoSuchElementException e) { // если объект не будет найден
            e.printStackTrace();
            return new ResponseEntity("id=" + id + " not found", HttpStatus.NOT_ACCEPTABLE);
        }

        return ResponseEntity.ok(category);
    }

}
