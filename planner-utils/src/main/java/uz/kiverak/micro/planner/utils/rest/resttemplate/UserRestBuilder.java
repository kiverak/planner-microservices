package uz.kiverak.micro.planner.utils.rest.resttemplate;

import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import uz.kiverak.micro.planner.plannerentity.entity.User;

@Component
public class UserRestBuilder {

    private static final String baseUrl = "http://localhost:8765/planner-users/user";

    public boolean userExists(Long userId) {
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<Long> request = new HttpEntity<>(userId);

        ResponseEntity<User> response;
        try {
            response = restTemplate.exchange(baseUrl + "/id", HttpMethod.POST, request, User.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
}
