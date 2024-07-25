package com.revature.models;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "Quizzes", schema = "project2")
@Data
public class Quiz {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "quiz_id")
    private int quizId;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private int courseId;

    private String title;

    private int timer;

    private int attemptsAllowed;

}
