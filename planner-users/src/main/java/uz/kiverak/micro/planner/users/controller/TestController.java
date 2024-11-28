package uz.kiverak.micro.planner.users.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/test")
public class TestController {

    @GetMapping()
    public String test() {
        return "test planner-users";
    }

}
