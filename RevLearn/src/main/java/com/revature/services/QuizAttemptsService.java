package com.revature.services;

import java.util.List;

import com.revature.exceptions.*;
import com.revature.models.QuizAttempts;
import com.revature.models.dtos.QuizAttemptsDTO;
import com.revature.models.dtos.QuizAttemptsEditDTO;

public interface QuizAttemptsService {
	
	// Create -------------------------
	
	// Create, but may perform an exception if attempt isn't allowed.
	// creates a timestamp in constructor of entity if the given timestamp is null.
	QuizAttempts create(QuizAttemptsDTO newDTOEntry) throws MaximumAllowedQuizAttemptsException;

	// Retrieve -------------------------
	
	// Get all entries.
	List<QuizAttempts> getAll();
	
	// Get single entry by its id.
	QuizAttempts getById(int quizAttempts_id);
	 
	// get all entries for a quiz via quizId.
	List<QuizAttempts> getAllByQuizId(Integer quiz_id);
	
	// get all entries for a student via userId.
	List<QuizAttempts> getAllByStudentId(Integer student_id);
	
	// get all entries for a course via CourseId
	List<QuizAttempts> getAllByCourseId(Integer course_id);
	
	// get all entries for a student's quiz via quizId and UserId.
	List<QuizAttempts> getAllByStudentIdAndQuizId(Integer quiz_id, Integer student_id);
	


	
	// Update -------------------------
	
	// updates an entry, does not update time; good for an adjustment.
	QuizAttempts updateByIdNoTime(int quizAttempts_id, QuizAttemptsEditDTO newData) throws BadRequestException, MaximumAllowedQuizAttemptsException;
	
	// updates an entry, updates time. good for a more thorough adjustment.
	QuizAttempts updateByIdWithTime(int quizAttempts_id, QuizAttemptsEditDTO newData) throws BadRequestException, MaximumAllowedQuizAttemptsException;
	
	// Delete -------------------------

	// delete single entry by its id.
	Integer deleteById(int quizAttempts_id);
	
	// Business
	
	public QuizAttempts verifyFieldsExistingEntry(QuizAttemptsEditDTO DTOtoEntity, Integer id);
	
	public QuizAttempts verifyFieldsNewEntry(QuizAttemptsDTO DTOtoEntity, Integer id);
	
	public boolean isQuizAttemptsAllowed(Integer quiz_id, Integer student_id);
	
}
