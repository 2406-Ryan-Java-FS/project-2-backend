package com.revature.models.dtos;

import lombok.Data;

@Data
public class QuizDTO {
    private int course_id;
    private String title;
    private int timer;
    private int attempts_allowed;
    private boolean open;
    private QuizQuestionDTO[] questions;
}

//{
//        "course_id": 1,
//        "title": "Java Basics Week 2 Quiz",
//        "timer": 60,
//        "attempts_allowed": 1,
//        "open": true,
//        "questions": [{
//        "question_text": "Example Question",
//        "question_choices": [{
//        "correct": true,
//        "text": "Example true answer"
//        },
//        {
//        "correct": false,
//        "text": "Example false answer"
//        }]
//        }]
//        }