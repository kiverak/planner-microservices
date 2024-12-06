package uz.kiverak.micro.planner.todo.mq.func;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import uz.kiverak.micro.planner.todo.service.TestDataService;

import java.util.function.Consumer;

@Configuration
public class MessageFunc {

    private TestDataService testDataService;

    public MessageFunc(TestDataService testDataService) {
        this.testDataService = testDataService;
    }

    @Bean
    public Consumer<Message<Long>> newUserActionConsume() {
        return message -> testDataService.initUserData(message.getPayload());
    }
}
