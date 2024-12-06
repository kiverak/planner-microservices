package uz.kiverak.micro.planner.users.mq.legacy;
//
//import org.springframework.messaging.Message;
//import org.springframework.messaging.support.MessageBuilder;
//import org.springframework.stereotype.Component;
//
//@Component
//@EnableBinding(TodoBinding.class)
//public class MessageProducer {
//
//    private TodoBinding todoBinding;
//
//    public MessageProducer(TodoBinding todoBinding) {
//        this.todoBinding = todoBinding;
//    }
//
//    public void initUserData(Long userId) {
//
//        Message<Long> message = MessageBuilder.withPayload(userId).build();
//
//        todoBinding.todoOutputChannel().send(message);
//    }
//}
