package uz.kiverak.micro.planner.utils.rest.webclient;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import uz.kiverak.micro.planner.plannerentity.entity.User;

@Component
public class UserWebclientBuilder {

    private static final String baseUrl = "http://localhost:8765/planner-users/user/";
    private static final String baseUrlData = "http://localhost:8765/planner-todo/data/";

    public boolean userExists(Long userId) {
        try {
            User user = WebClient.create(baseUrl)
                    .post()
                    .uri("id")
                    .bodyValue(userId)
                    .retrieve()
                    .bodyToFlux(User.class)
                    .blockFirst();

            if (user != null) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public Flux<User> userExistsAsync(Long userId) {

        return WebClient.create(baseUrl)
                .post()
                .uri("id")
                .bodyValue(userId)
                .retrieve()
                .bodyToFlux(User.class);
    }

    public Flux<Boolean> initUserData(Long userId) {

        return WebClient.create(baseUrlData)
                .post()
                .uri("init")
                .bodyValue(userId)
                .retrieve()
                .bodyToFlux(Boolean.class);
    }
}
