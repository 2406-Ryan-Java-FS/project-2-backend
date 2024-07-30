package com.revature.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.Instant;

@Entity
@Table(name="quizattempts", schema = "project2")
@Data
@AllArgsConstructor
@NoArgsConstructor
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
	private Timestamp attempt_date;
	
	@Column(name="score", nullable = false, columnDefinition = "numeric(4,2) check (score <= 100)")
	private Double score;
	
	

	// for DTO
	public QuizAttempts(User student, Quiz quiz, Timestamp attempt_date, Double score) 
	{
		super();
		this.student = student;
		this.quiz = quiz;
		
		// for brand new entries
		if(this.attempt_date == null && attempt_date == null)
			this.attempt_date = Timestamp.from(Instant.now());
		// for entries attempting to update this field.
		else if (attempt_date != null)
			this.attempt_date = attempt_date;
		
		this.score = score;
	}

	
	
}

