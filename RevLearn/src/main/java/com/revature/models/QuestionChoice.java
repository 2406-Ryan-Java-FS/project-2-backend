package com.revature.models;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "questionchoices", schema = "project2")
@Data
public class QuestionChoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "choice_id")
    private int id;

    @Column(name = "question_id")
    private int questionId;

    @Column(name = "text")
    private String choiceText;

    @Column(name = "correct")
    private boolean correct;
}
