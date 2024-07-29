package com.revature.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "Quizzes", schema = "project2")
@Data
@JsonPropertyOrder({"quizId","courseId","title","timer","attemptsAllowed"})
public class Quiz {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "quiz_id")
    @JsonProperty(value = "quizId")
    private int quizId;
    
    @Column(name = "course_id")
    @JsonProperty(value = "courseId")
    private int courseId;

    @JsonProperty(value = "title")
    private String title;

    @JsonProperty(value = "timer")
    private int timer;

    @JsonProperty(value = "attemptsAllowed")
    private int attemptsAllowed;

}

