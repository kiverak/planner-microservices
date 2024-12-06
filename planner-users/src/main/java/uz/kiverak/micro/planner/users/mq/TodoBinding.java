package uz.kiverak.micro.planner.users.mq;

import org.springframework.messaging.MessageChannel;
import org.springframework.cloud.stream.annotation.Output;

public interface TodoBinding {

    String OUTPUT_CHANNEL = "todoOutputChannel";

    @Output(OUTPUT_CHANNEL)
    MessageChannel todoOutputChannel();
}
