package com.revature.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import com.revature.models.Course;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;

@Service
public class KafkaConsumerService{

    @Autowired
    CourseService courseService;

    @KafkaListener(topics = "addCourse", groupId = "course-listeners-adding")
    public void listenForNewCourses(Course course){
        System.out.println("Received message: " + course);
        System.out.println("Added course");
    }

    @KafkaListener(topics = "findCourse", groupId = "course-listeners-adding")
    public void listenForFindCourses(Course course){
        System.out.println("Received message: " + course);
        System.out.println("Found Course");
    }
}