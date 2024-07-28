package com.revature.controllers;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Profile({"h2","awstest"})
@RestController
public class KafkaPractice {

    private static final Logger logger= LoggerFactory.getLogger(KafkaPractice.class);

    @Autowired KafkaTemplate<String,String> kafkaTemplate;

    @PostConstruct
    public void doneBeans(){
        kafkaTemplate.send("pingTopic","Rev Learn has started!!");
    }

    @GetMapping("/kafka/{messageToSend}")
    public void pingTheBroker(@PathVariable String messageToSend){
        logger.info("Going to ping the broker: "+StringUtils.truncate(messageToSend,100));
        kafkaTemplate.send("pingTopic",messageToSend);
    }

    @KafkaListener(topics = "pingTopic")
    public void pingListen(String incomingMessage){
        logger.info("Receiving message "+incomingMessage);
    }
}
