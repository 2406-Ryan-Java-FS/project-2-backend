package com.revature.services;

import java.util.List;

import org.apache.coyote.BadRequestException;

import com.revature.models.QuizAttempts;

public interface QuizAttemptsService {
	
	// Basic CRUD
	
	// Add an entry to the database, where the id will be autmatically incrememnted.
	QuizAttempts create(QuizAttempts newEntry) throws BadRequestException;
	
	// Get all QuizAttempts
	List<QuizAttempts> getAll();
	
	// Get a quizAttempt based on a quizAttempt id
	QuizAttempts getById(int quizAttempts_id) throws BadRequestException;
	
	// Update a quizAttempt based on a quizAttempt id
	QuizAttempts updateById(int quizAttempts_id, QuizAttempts newData) throws BadRequestException;
	
	// Delete a quizAttempt base on a quizAttempt id;
	Integer deleteById(int quizAttempts_id);
	
	// to be implemented later
	// - QuizAttempts via user id
	// - QuizAttempts via quiz id
	// - QuizAttempts via user and quiz id

}
