package com.revature.models;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "quizzes",schema = "project2")
@Data
public class Quiz {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer quiz_id;

}
