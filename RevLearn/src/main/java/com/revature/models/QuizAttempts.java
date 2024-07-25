package com.revature.models;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Date;

@Entity
@Table(name="QuizAttempts")
@Data
public class QuizAttempts {

	
	
	@Id 
	@GeneratedValue (strategy = GenerationType.AUTO)
	@Column (name="quizAttempt_id", nullable = false)
	@ManyToOne
    private Integer quizAttempt_id;
	
	@Column(name="student_id", nullable = false)
	private User student;
	
	@Column (name="quiz_id", nullable = false, columnDefinition = "int")
	@ManyToOne
    private Quiz quiz;
	
	@Column(name="attempt_date", nullable = false, columnDefinition = "timestamp")
	private Date attempt_date;
	
	@Column(name="score", nullable = false, columnDefinition = "numeric(4,2")
	private double score;
	
	public QuizAttempts(User student, Quiz quiz, Double score) 
	{
		super();
		this.student = student;
		this.quiz = quiz;
		this.attempt_date = Timestamp.from(Instant.now());
		this.score = score;
	}
	
}

