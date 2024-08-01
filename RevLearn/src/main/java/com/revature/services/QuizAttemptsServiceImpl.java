package com.revature.services;

import java.util.List;

import org.apache.commons.math3.util.Precision;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

// Local
import com.revature.models.QuizAttempts;
import com.revature.DTO.QuizAttemptsDTO;
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
		if(!isQuizAttemptsAllowed(newDTOEntry.getQuiz_id(), newDTOEntry.getStudent_id()))
			throw new MaximumAllowedQuizAttemptsException("You do not have remaining chances to attempt this quiz.");
		
		QuizAttempts newEntity = verifyFields(newDTOEntry, 0);
		
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
	public List<QuizAttempts> getAllByStudentIdAndQuizId(Integer student_id, Integer quiz_id) {
		return attemptsRepo.findByStudent_UserIdAndQuiz_QuizId(student_id, quiz_id);
	}
	
	////////////////////////////
	//---------Update---------//
	////////////////////////////
	
	// Update Entries by id, this assumes we do not update the time.
	public QuizAttempts updateByIdNoTime(int quizAttempts_id, QuizAttemptsDTO newData) 
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
		
		QuizAttempts updatedEntity = verifyFields(newData, quizAttempts_id);
						
		return attemptsRepo.save(updatedEntity);
	}
	
	// Update Entries by id, this assumes we do not update the time.
	public QuizAttempts updateByIdWithTime(int quizAttempts_id, QuizAttemptsDTO newData) 
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
		 
		QuizAttempts updatedEntity = verifyFields(newData, quizAttempts_id);
						
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
	
	public Integer deleteAllByQuizId(Integer quiz_id) {
		try 
		{
			attemptsRepo.deleteAllByQuiz_QuizId(quiz_id);
		} 
		catch (Exception e)
		{
			e.getMessage();
			return 0;
		}
		
		return 1;	
	}
	
	public Integer deleteAllByStudentId(Integer student_id) {
		try 
		{
			attemptsRepo.deleteAllByStudent_UserId(student_id);
		} 
		catch (Exception e)
		{
			e.getMessage();
			return 0;
		}
		
		return 1;	
	}
	
	@Override
	public Integer deleteAllByQuizAndStudent(Integer quiz_id, Integer student_id) {
		try 
		{
			attemptsRepo.deleteAllByQuiz_QuizIdAndStudent_UserId(quiz_id, student_id);
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
	//    map a QuizAttemptsDTO to a QuizAttempts entity.
	// - if given id field is 0, then this is a new entry.
	// - if given id field is nonzero, then is an existing entry.
	public QuizAttempts verifyFields(QuizAttemptsDTO DTOtoEntity, Integer id) 
			throws BadRequestException
	{
		
		// Check if the entry's student/quiz/date is not null.
		if(!userRepo.existsById(DTOtoEntity.getStudent_id()))
			throw new BadRequestException("Entry is not related to a user and may not be related to a quiz either.");
		if (!quizRepo.existsById(DTOtoEntity.getQuiz_id()))
			throw new BadRequestException("Entry is not related to a quiz.");
		if (DTOtoEntity.getAttempt_date() == null && id != 0)
			throw new BadRequestException("Entry does not have a saved date.");
		
		// Ensure decimal has at most 2 decimal places by rounding:
		// Precision is injected via the pom under "org.apache.commons" "commons-math3"
		if(DTOtoEntity.getScore() > 100)
			throw new BadRequestException("Entry has a score above 100");
		DTOtoEntity.setScore(Precision.round(DTOtoEntity.getScore(), 2));
			
		QuizAttempts validatedData;
		if(id == 0)
			validatedData = new QuizAttempts(userRepo.findById(DTOtoEntity.getStudent_id()).get(), quizRepo.findById(DTOtoEntity.getQuiz_id()).get(), DTOtoEntity.getAttempt_date(), DTOtoEntity.getScore());
		else
			validatedData = new QuizAttempts(id, userRepo.findById(DTOtoEntity.getStudent_id()).get(), quizRepo.findById(DTOtoEntity.getQuiz_id()).get(), DTOtoEntity.getAttempt_date(), DTOtoEntity.getScore());

		return validatedData;
	}
	
	
	public boolean isQuizAttemptsAllowed(Integer quiz_id, Integer student_id) {
		// If the number of attempts linked to a quiz and student are less than the number allowed...
		if(attemptsRepo.countByQuiz_QuizIdAndStudent_UserId(quiz_id, student_id) < quizRepo.findById(quiz_id).get().getAttemptsAllowed())
			return true; // Then we can return true;
		
		// Otherwise, return false;
		return false;
	}




}
