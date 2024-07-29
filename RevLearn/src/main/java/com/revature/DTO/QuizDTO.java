package com.revature.DTO;

import com.revature.models.QuizQuestion;
import lombok.Data;

@Data
public class QuizDTO {
    private int courseId;
    private String title;
    private int timer;
    private int attemptsAllowed;
    private boolean open;
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