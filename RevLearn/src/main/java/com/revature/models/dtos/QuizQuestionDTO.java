package com.revature.models.dtos;

import com.revature.models.QuestionChoice;
import lombok.Data;

@Data
public class QuizQuestionDTO {
    private String question_text;
    private QuestionChoice[] question_choices;
}


//        "question_text": "Example Question",
//        "question_choices":