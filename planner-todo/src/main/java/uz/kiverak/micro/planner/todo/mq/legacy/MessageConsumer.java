package uz.kiverak.micro.planner.todo.mq.legacy;
//
//import org.springframework.stereotype.Component;
//import uz.kiverak.micro.planner.todo.service.TestDataService;
//
//@Component
//@EnableBinding(TodoBinding.class)
//public class MessageConsumer {
//
//    private final TestDataService testDataService;
//
//    public MessageConsumer(TestDataService testDataService) {
//        this.testDataService = testDataService;
//    }
//
//    @StreamListener(target = TodoBinding.INPUT_CHANNEL)
//    public void initTestData(String userId) throws Exception {
//        throw new Exception("Test dlq");
////        testDataService.initTestData(userId);
//    }
//}
