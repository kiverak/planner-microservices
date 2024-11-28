package uz.kiverak.micro.planner.users;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableDiscoveryClient
@EntityScan(basePackages = {"uz.kiverak.micro.planner"})
@ComponentScan(basePackages = {"uz.kiverak.micro.planner.**"})
public class PlannerUsersApplication {

    public static void main(String[] args) {
        SpringApplication.run(PlannerUsersApplication.class, args);
    }

}
