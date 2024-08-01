package com.revature.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducerService {


    @Autowired
    KafkaTemplate<String, String> kafkaTemplate;


    public void sendRequestMessage(String requestMessage){

        Message<String> message = MessageBuilder
                .withPayload(requestMessage)
                .setHeader(KafkaHeaders.TOPIC, "request")
                .build();

        kafkaTemplate.send(message);
    }

    public void sendResponseMessage(String responseMessage){
        Message<String> message = MessageBuilder
                .withPayload(responseMessage)
                .setHeader(KafkaHeaders.TOPIC, "response")
                .build();
        kafkaTemplate.send(message);
    }
}