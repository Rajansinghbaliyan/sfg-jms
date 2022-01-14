package guru.springframework.sfgjms.sender;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import guru.springframework.sfgjms.config.JmsConfig;
import guru.springframework.sfgjms.model.HelloWorldMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import java.io.IOException;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Component
public class HelloMessageSender {
    private final JmsTemplate jmsTemplate;
    private final ObjectMapper objectMapper;

//    @Scheduled(fixedDelay = 3000)
//    public void messageSender(){
//        HelloWorldMessage message = HelloWorldMessage.builder()
//                .uuid(UUID.randomUUID())
//                .message("This is Hello World")
//                .build();
//
//        jmsTemplate.convertAndSend(JmsConfig.MY_QUEUE, message);
//
//        log.debug("Message send from the scheduler: HelloMessageSender");
//    }

//    @Scheduled(fixedDelay = 1000)
//    public void messageSendAndReceive() throws JMSException, IOException {
//        HelloWorldMessage message = HelloWorldMessage.builder()
//                .uuid(UUID.randomUUID())
//                .message("This is Hello World")
//                .build();
//
//        Message receivedMessage = jmsTemplate.sendAndReceive(JmsConfig.MY_SEND_AND_RECEIVE_QUEUE, new MessageCreator() {
//            @Override
//            public Message createMessage(Session session) throws JMSException {
//                Message helloMessage = null;
//                try {
//                    helloMessage = session.createTextMessage(objectMapper.writeValueAsString(message));
//                    helloMessage.setStringProperty("_type","guru.springframework.sfgjms.model.HelloWorldMessage");
//
//                    log.debug("Message send from the SendAndReceiver");
//                    return helloMessage;
//                } catch (JsonProcessingException e) {
//                    throw new JMSException("Boom");
//                }
//            }
//        });
//
//        HelloWorldMessage msg = objectMapper.readValue(receivedMessage.getBody(String.class),HelloWorldMessage.class);
//
//        log.debug("Message Received form the send and received :"+receivedMessage.getBody(String.class));
//    }

//    @Scheduled(fixedRate = 3000)
    public void sendHelloMessage() throws JsonProcessingException {
        HelloWorldMessage message = HelloWorldMessage.builder()
                .uuid(UUID.randomUUID())
                .message("Event triggered")
                .build();

        jmsTemplate.convertAndSend(JmsConfig.MY_QUEUE,message);
        log.debug("Message Sent From HelloSender: "+message.getUuid());
    }

    @Scheduled(fixedRate = 2000)
    public void sendAndReceive() throws JMSException, IOException {
        HelloWorldMessage message = HelloWorldMessage.builder()
                .uuid(null)
                .message("Save object")
                .build();

        Message receivedMsg = jmsTemplate.sendAndReceive(JmsConfig.MY_SEND_AND_RECEIVE_QUEUE, new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                try {
                    Message msg = session.createTextMessage(objectMapper.writeValueAsString(message));
                    msg.setStringProperty("_type","guru.springframework.sfgjms.model.HelloWorldMessage");
                    log.debug("Message Send from sendAndReceive : " + message.getUuid());
                    return msg;
                } catch (JsonProcessingException e) {
                    throw new JMSException("Boom");
                }
            }
        });

        HelloWorldMessage parsedMsg = objectMapper.readValue(receivedMsg.getBody(String.class),HelloWorldMessage.class);

        log.debug("Message received in sendAndReceive: " + parsedMsg.getUuid());
    }
}
