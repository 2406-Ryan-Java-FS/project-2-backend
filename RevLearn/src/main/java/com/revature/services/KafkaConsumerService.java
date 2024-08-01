package com.revature.services;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService {

    @KafkaListener(topics = "request", groupId = "request-listener")
    public void consumeRequestMessage(String message) {
        System.out.println("Request sent: \n" + message);
    }

    @KafkaListener(topics = "response", groupId = "response-listener")
    public void consumeResponseMessage(String message) {
        System.out.println("Response received: \n" + message);
    }
}