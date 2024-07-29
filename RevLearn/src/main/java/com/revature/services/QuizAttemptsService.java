package com.revature.services;

import java.util.List;
import com.revature.exceptions.*;
import com.revature.DTO.QuizAttemptsDTO;
import com.revature.models.QuizAttempts;

public interface QuizAttemptsService {
	
	// Basic CRUD
	
	// Add an entry to the database, where the id will be autmatically incrememnted.
	QuizAttempts create(QuizAttemptsDTO newEntry) throws BadRequestException;
	
	// Get all QuizAttempts
	List<QuizAttempts> getAll();
	
	// Get a quizAttempt based on a quizAttempt id
	QuizAttempts getById(int quizAttempts_id) throws BadRequestException;
	
	// Update a quizAttempt based on a quizAttempt id
	QuizAttempts updateByIdNoTime(int quizAttempts_id, QuizAttemptsDTO newData) throws BadRequestException;
	QuizAttempts updateByIdWithTime(int quizAttempts_id, QuizAttemptsDTO newData) throws BadRequestException;
	// Delete a quizAttempt base on a quizAttempt id;
	Integer deleteById(int quizAttempts_id);
	
	// to be implemented later
	// - QuizAttempts via user id
	// - QuizAttempts via quiz id
	// - QuizAttempts via user and quiz id

}
