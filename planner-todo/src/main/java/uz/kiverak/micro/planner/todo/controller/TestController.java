package uz.kiverak.micro.planner.todo.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/test")
public class TestController {

    @GetMapping()
    public String test() {
        return "test";
    }

}
