package com.revature.models.dtos;

import lombok.Data;

@Data
public class QuizQuestionDTO {
    private String question_text;
    private QuestionChoiceDTO[] question_choices;
}


//        "question_text": "Example Question",
//        "question_choices":