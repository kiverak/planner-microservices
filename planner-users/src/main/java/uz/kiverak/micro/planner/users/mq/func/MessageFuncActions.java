package uz.kiverak.micro.planner.users.mq.func;

import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Sinks;

@Log4j2
@Service
@Getter
public class MessageFuncActions {

    private MessageFunc messageFunc;

    public MessageFuncActions(MessageFunc messageFunc) {
        this.messageFunc = messageFunc;
    }

    public void sendNewUserMessage(Long id) {
        messageFunc.getInnerBus().emitNext(MessageBuilder.withPayload(id).build(), Sinks.EmitFailureHandler.FAIL_FAST);
        log.info("Message sent: {}", id);
    }

}
