package com.revature.models;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "quizquestions")
@Data
public class QuizQuestion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "question_id")
    private int id;
    @Column(name = "quiz_id", nullable = false)
    private int quizId;
    @Column(name = "question_text", nullable = false)
    private String questionText;
//    @Column(name = "correct_answer", nullable = false)
//    private String correctAnswer;
//    @Column(name = "answer_options", nullable = false)
//    private String answerOptions;
}
