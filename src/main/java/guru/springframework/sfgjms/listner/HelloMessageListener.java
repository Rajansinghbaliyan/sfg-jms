package guru.springframework.sfgjms.listner;

import guru.springframework.sfgjms.config.JmsConfig;
import guru.springframework.sfgjms.model.HelloWorldMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import java.util.UUID;

@Component
@Slf4j
@RequiredArgsConstructor
public class HelloMessageListener {

    private final JmsTemplate jmsTemplate;

//    @JmsListener(destination = JmsConfig.MY_QUEUE)
//    public void listener(@Payload HelloWorldMessage helloWorldMessage,
//                         @Headers MessageHeaders headers,
//                         Message message) {
//
//        log.debug("Message Received in HelloMessageListener : " + helloWorldMessage);
//    }

//    @JmsListener(destination = JmsConfig.MY_SEND_AND_RECEIVE_QUEUE)
//    public void listenAndSend(@Payload HelloWorldMessage helloWorldMessage,
//                              @Headers MessageHeaders headers,
//                              Message message) throws JMSException {
//
//        log.debug("Message received by listen and send :"+helloWorldMessage);
//
//        HelloWorldMessage replyMessage = HelloWorldMessage.builder()
//                .uuid(UUID.randomUUID())
//                .message("Message Received Successfully")
//                .build();
//
//        jmsTemplate.convertAndSend(message.getJMSReplyTo(),replyMessage);
//    }


    @JmsListener(destination = JmsConfig.MY_QUEUE)
    public void listenerHelloMessage(@Payload HelloWorldMessage helloWorldMessage,
                                     @Headers MessageHeaders headers,
                                     Message message
                                     ){
        log.debug("Message Received in listenHelloMessage: "+helloWorldMessage.getUuid());
    }

    @JmsListener(destination = JmsConfig.MY_SEND_AND_RECEIVE_QUEUE)
    public void listenAndSend(@Payload HelloWorldMessage helloWorldMessage,
                              @Headers MessageHeaders headers,
                              Message message
                              ) throws JMSException {
        helloWorldMessage.setUuid(UUID.randomUUID());
        log.debug("Message is saved in the listenAndSend: "+helloWorldMessage.getUuid());

        jmsTemplate.convertAndSend(message.getJMSReplyTo(),helloWorldMessage);
    }
}
