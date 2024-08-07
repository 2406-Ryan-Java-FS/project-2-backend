package com.revature.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.revature.models.ChoiceSelection;
// Local
import com.revature.models.QuizAttempts;
import com.revature.models.dtos.QuizAttemptsDTO;
import com.revature.models.dtos.QuizAttemptsEditDTO;
import com.revature.repositories.ChoiceSelectionRepo;
import com.revature.repositories.QuestionChoiceRepo;
import com.revature.repositories.QuizAttemptsRepository;
import com.revature.repositories.QuizRepository;
import com.revature.repositories.UserRepository;
import com.revature.exceptions.*;

@Service
public class QuizAttemptsServiceImpl implements QuizAttemptsService{

	@Autowired
	QuizAttemptsRepository attemptsRepo;
	
	@Autowired
	UserRepository userRepo;
	
	@Autowired
	QuizRepository quizRepo;
	
	
	////////////////////////////
	//---------Create---------//
	////////////////////////////
	
	// Create an entry
	public QuizAttempts create(QuizAttemptsDTO newDTOEntry) 
			throws BadRequestException, MaximumAllowedQuizAttemptsException
	{		
		// Check if another entry is allowed...
		isQuizAttemptsAllowed(newDTOEntry.getQuiz_id(), newDTOEntry.getStudent_id());
		
		QuizAttempts newEntity = verifyFieldsNewEntry(newDTOEntry, 0);
		
		return attemptsRepo.save(newEntity);
	}
	
	////////////////////////////
	//--------Retrieve--------//
	////////////////////////////
	
	// Get all entries
	public List<QuizAttempts> getAll() 
	{
		return attemptsRepo.findAll();
	}
	
	// Get an entry by its id
	public QuizAttempts getById(int quizAttempts_id)
		throws BadRequestException 
	{
			if(!attemptsRepo.existsById(quizAttempts_id))
			throw new BadRequestException("QuizAttemptsService getById - entry with given id doesn't exist.");
		return attemptsRepo.findById(quizAttempts_id).get();
	}
	
	// Get all entries for a quiz
	public List<QuizAttempts> getAllByQuizId(Integer quiz_id) 
	{	
		return attemptsRepo.findAllByQuiz_QuizId(quiz_id);
	}
	
	// Get all entries for a student
	public List<QuizAttempts> getAllByStudentId(Integer student_id) 
	{
		return attemptsRepo.findAllByStudent_UserId(student_id);
	}
	
	// get all entries for a student's quiz.
	public List<QuizAttempts> getAllByStudentIdAndQuizId(Integer quiz_id, Integer student_id) {
		return attemptsRepo.findAllByQuiz_QuizIdAndStudent_UserId(quiz_id, student_id);
	}
	
	public List<QuizAttempts> getAllByCourseId(Integer course_id) {
		return attemptsRepo.findAllByQuiz_CourseId(course_id);
	}
	
	
	////////////////////////////
	//---------Update---------//
	////////////////////////////
	
	// Update Entries by id, this assumes we do not update the time.
	public QuizAttempts updateByIdNoTime(int quizAttempts_id, QuizAttemptsEditDTO newData) 
			throws BadRequestException, MaximumAllowedQuizAttemptsException
	{
		// This is update, not create. Only perform if the entry with id exists.
		if(!attemptsRepo.existsById(quizAttempts_id))
			throw new BadRequestException("QuizAttemptsService updateById - entry with given id doesn't exist.");
		
		QuizAttempts updateTarget = attemptsRepo.findById(quizAttempts_id).get();
		
		// Update has the capacity to illegally exceed maximum attempts.
		// If an attempt is being placed under a different quiz...
		if(updateTarget.getQuiz().getQuizId() != newData.getQuiz_id())
		{
			//... then make sure there is room for another entry.
			if(!isQuizAttemptsAllowed(quizAttempts_id, newData.getStudent_id()))
				throw new MaximumAllowedQuizAttemptsException("Update results in exceeded attempts allowed. abandoning updateByIdNoTime.");
		}
		
		// This method assumes that time will not be updated, so we'll use the current one.
		newData.setAttempt_date(updateTarget.getAttempt_date());
		
		QuizAttempts updatedEntity = verifyFieldsExistingEntry(newData, quizAttempts_id);
						
		return attemptsRepo.save(updatedEntity);
	}
	
	// Update Entries by id, this assumes we do not update the time.
	public QuizAttempts updateByIdWithTime(int quizAttempts_id, QuizAttemptsEditDTO newData) 
			throws BadRequestException, MaximumAllowedQuizAttemptsException
	{
		
		// This is update, not create. Only perform if the entry with id exists.
		if(!attemptsRepo.existsById(quizAttempts_id))
			throw new BadRequestException("QuizAttemptsService updateById - entry with given id doesn't exist.");
				
		QuizAttempts updateTarget = attemptsRepo.findById(quizAttempts_id).get();
		
		// Update has the capacity to illegally exceed maximum attempts.
		// If an attempt is being placed under a different quiz...
		if(updateTarget.getQuiz().getQuizId() != newData.getQuiz_id())
		{
			//... then make sure there is room for another entry.
			if(!isQuizAttemptsAllowed(quizAttempts_id, newData.getStudent_id()))
				throw new MaximumAllowedQuizAttemptsException("Update results in exceeded attempts allowed. abandoning updateByIdWithTime.");
		}
		 
		QuizAttempts updatedEntity = verifyFieldsExistingEntry(newData, quizAttempts_id);
						
		return attemptsRepo.save(updatedEntity);
	}
	
	////////////////////////////
	//---------Delete---------//
	////////////////////////////

	public Integer deleteById(int quizAttempts_id) 
	{
		//if(!attemptsRepo.existsById(quizAttempts_id))
		//	return 0;
		try 
		{
			attemptsRepo.deleteById(quizAttempts_id);
		} 
		catch (Exception e)
		{
			e.getMessage();
			return 0;
		}
		
		return 1;	
	}
	
	@SuppressWarnings("deprecation")
	public Integer deleteAllByQuizId(Integer quiz_id) {
		try 
		{
			attemptsRepo.deleteInBatch(attemptsRepo.findAllByQuiz_QuizId(quiz_id));
		} 
		catch (Exception e)
		{
			e.getMessage();
			return 0;
		}
		
		return 1;	
	}
	
	@SuppressWarnings("deprecation")
	public Integer deleteAllByStudentId(Integer student_id) {
		try 
		{
			attemptsRepo.deleteInBatch(attemptsRepo.findAllByStudent_UserId(student_id));
		} 
		catch (Exception e)
		{
			e.getMessage();
			return 0;
		}
		
		return 1;	
	}
	
	@SuppressWarnings("deprecation")
	public Integer deleteAllByCourseId(Integer course_id) {
		try 
		{
			attemptsRepo.deleteInBatch(attemptsRepo.findAllByQuiz_CourseId(course_id));
		} 
		catch (Exception e)
		{
			e.getMessage();
			return 0;
		}
		
		return 1;	
	}
	
	public Integer deleteAllByQuizAndStudent(Integer quiz_id, Integer student_id) {
		try 
		{
			attemptsRepo.deleteAllInBatch(attemptsRepo.findAllByQuiz_QuizIdAndStudent_UserId(quiz_id, student_id));
		}
		catch (Exception e)
		{
			e.getMessage();
			return 0;
		}
		
		return 1;	
	}
	
	// Business
	

	// Verify incoming fields from the front end and 
	//    map a QuizAttemptsDTO to a NEW QuizAttempts entity.
	// - if given id field is nonzero, then is an existing entry.
	public QuizAttempts verifyFieldsNewEntry(QuizAttemptsDTO DTOtoEntity, Integer id) 
			throws BadRequestException
	{
		
		// Check if the entry's student/quiz/date is not null.
		if(!userRepo.existsById(DTOtoEntity.getStudent_id()))
			throw new BadRequestException("Entry is not related to a user and may not be related to a quiz either.");
		if (!quizRepo.existsById(DTOtoEntity.getQuiz_id()))
			throw new BadRequestException("Entry is not related to a quiz.");
		if (DTOtoEntity.getAttempt_date() == null && id != 0)
			throw new BadRequestException("Entry does not have a saved date.");
		
		return new QuizAttempts(userRepo.findById(DTOtoEntity.getStudent_id()).get(), quizRepo.findById(DTOtoEntity.getQuiz_id()).get(), DTOtoEntity.getAttempt_date());

	}
	
	// Verify incoming fields from the front end and 
	//    map a QuizAttemptsDTO to an EXISTING QuizAttempts entity.
	public QuizAttempts verifyFieldsExistingEntry(QuizAttemptsEditDTO DTOtoEntity, Integer id) 
			throws BadRequestException
	{
		
		// Check if the entry's student/quiz/date is not null.
		if(!userRepo.existsById(DTOtoEntity.getStudent_id()))
			throw new BadRequestException("Entry is not related to a user and may not be related to a quiz either.");
		if (!quizRepo.existsById(DTOtoEntity.getQuiz_id()))
			throw new BadRequestException("Entry is not related to a quiz.");
		if (DTOtoEntity.getAttempt_date() == null && id != 0)
			throw new BadRequestException("Entry does not have a saved date.");
		
			
		QuizAttempts validatedData;

		
		// Ensure decimal has at most 2 decimal places by rounding:
		// Precision is injected via the pom under "org.apache.commons" "commons-math3"
		if(DTOtoEntity.getScore() > 100)
			throw new BadRequestException("Entry has a score above 100");
			
		validatedData = new QuizAttempts(id, userRepo.findById(DTOtoEntity.getStudent_id()).get(), quizRepo.findById(DTOtoEntity.getQuiz_id()).get(), DTOtoEntity.getAttempt_date(), DTOtoEntity.getScore());

		return validatedData;
	}
	
	public boolean isQuizAttemptsAllowed(Integer quiz_id, Integer student_id) {
		
		// If the number of attempts linked to a quiz and student are less than the number allowed...
		if(attemptsRepo.countByQuiz_QuizIdAndStudent_UserId(quiz_id, student_id) >= quizRepo.findById(quiz_id).get().getAttemptsAllowed())
			throw new ConflictException("Remaining chances have been used up.");
		
		if(!quizRepo.findById(quiz_id).get().isOpen())
			throw new ConflictException("Quiz is closed.");
		
		return true;

	}

}
