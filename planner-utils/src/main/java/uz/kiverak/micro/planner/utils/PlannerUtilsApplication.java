package uz.kiverak.micro.planner.utils;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "uz.kiverak.micro.planner.**")
public class PlannerUtilsApplication {

    public static void main(String[] args) {
        SpringApplication.run(PlannerUtilsApplication.class, args);
    }

}
