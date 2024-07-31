package com.revature.models.dtos;

import lombok.Data;

@Data
public class QuestionChoiceDTO {
    private String text;
    private Boolean correct;
}

//"question_choices": [{
//        "correct": true,
//        "text": "Example true answer"
//        }