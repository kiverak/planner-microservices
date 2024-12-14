package uz.kiverak.micro.planner.todo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.kiverak.micro.planner.plannerentity.entity.Stat;
import uz.kiverak.micro.planner.todo.service.StatService;

@RestController
public class StatController {

    private final StatService statService;

    public StatController(StatService statService) {
        this.statService = statService;
    }

    @PostMapping("/stat")
    public ResponseEntity<Stat> findByEmail(@RequestBody String userId) {

        return ResponseEntity.ok(statService.findStat(userId));
    }


}
