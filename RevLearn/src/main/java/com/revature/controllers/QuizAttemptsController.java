package com.revature.controllers;

public interface QuizAttemptsController {
	// To be implemented later.
	
	// probable endpoints:
	
	//-----------------CRUD-----------------//
	//
	// POST   on /newQuizAttempts
	// GET    on /quizAttempts
	//           /quizAttempts/{quizAttempt_id}
	// PATCH  on /quizAttempts/{quizAttempt_id}
	// DELETE on /quizAttempts/{quizAttempt_id}
	
	//for later consideration:
	
	//---------------------FROM quiz or user--------------------//
	//
	// - For retrieving all attempts for a quiz:
	// GET     on /quizAttemptsFromQuiz/{quiz_id}
	// - For deleting all attempts for a quiz:
	// DELETE  on /quizAttemptsFromQuiz/{quiz_id}
	//
	// - For retrieving all attempts for a user (probably student):
	// GET     on /quizAttemptsFromUser/{user_id}
	// - For updating an attempt for a user (probably student):
	// PATCH   on /quizAttemptsFromUser/{user_id}
	// - For deleting all attempts for a user (probably student):
	// DELETE  on /quizAttemptsFromUser/{user_id}
	
	// Anything more, Please Ask Steven Coronel
	

	
}
