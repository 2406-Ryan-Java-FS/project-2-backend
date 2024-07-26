package com.revature.models;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Date;

@Entity
@Table(name="quizattempts", schema = "project2")
@Data
public class QuizAttempts {

	@Id 
	@GeneratedValue (strategy = GenerationType.IDENTITY)
	@Column (name="attempt_id", nullable = false)
    private Integer quizAttempts_id;

	@ManyToOne
	@JoinColumn(name="student_id", referencedColumnName = "user_id", nullable = false)
	private User student;

	@ManyToOne
	@JoinColumn (name="quiz_id", nullable = false, columnDefinition = "int")
    private Quiz quiz;
	
	@Column(name="attempt_date", nullable = false, columnDefinition = "timestamp")
	private Date attempt_date;
	
	@Column(name="score", nullable = false, columnDefinition = "numeric(4,2)")
	private Double score;
	
	
	
	public QuizAttempts(User student, Quiz quiz, Double score) 
	{
		super();
		this.student = student;
		this.quiz = quiz;
		this.attempt_date = Timestamp.from(Instant.now());
		this.score = score;
	}



	public QuizAttempts(Integer quizAttempts_id, User student, Quiz quiz, Date attempt_date, Double score) {
		super();
		this.quizAttempts_id = quizAttempts_id;
		this.student = student;
		this.quiz = quiz;
		this.attempt_date = attempt_date;
		this.score = score;
	}

	
}

