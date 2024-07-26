package com.revature.services;

import java.util.List;

import org.apache.commons.math3.util.Precision;
import org.springframework.beans.factory.annotation.Autowired;

// Local
import com.revature.models.QuizAttempts;
import com.revature.repositories.QuizAttemptsRepository;
import com.revature.exceptions.*;

public class QuizAttemptsServiceImpl implements QuizAttemptsService{

	@Autowired
	QuizAttemptsRepository attemptsRepo;
	
	///////-------Basic CRUD--------///////
	
	// Create an entry
	public QuizAttempts create(QuizAttempts newEntry) 
			throws BadRequestException 
	{
		if(!verifyFields(newEntry))
			System.out.println("QuizAttemptsServiceImpl create method: validation didn't pass for new entry."); //should be unreachable
		
		return attemptsRepo.save(newEntry);
	}

	// Get all entries
	public List<QuizAttempts> getAll() 
	{
		
		return attemptsRepo.findAll();
	}

	// Get all entries by id
	public QuizAttempts getById(int quizAttempts_id)
		throws BadRequestException
	{

		if(!attemptsRepo.existsById(quizAttempts_id))
			throw new com.revature.exceptions.BadRequestException("QuizAttempts with given id doesn't exist");
		return attemptsRepo.findById(quizAttempts_id).get();
	}

	// Update Entries by id
	public QuizAttempts updateById(int quizAttempts_id, QuizAttempts newData) 
	{
		if(!verifyFields(newData))
			System.out.println("QuizAttemptsServiceImpl update method: validation didn't pass for updated entry."); //should be unreachable
		
		// I still need to figure out how to handle date time. May need adjusting.
		QuizAttempts updatedEntry = new QuizAttempts(quizAttempts_id, newData.getStudent(), newData.getQuiz(), newData.getAttempt_date(), newData.getScore());
				
		return attemptsRepo.save(updatedEntry);
	}

	@Override
	public Integer deleteById(int quizAttempts_id) 
	{
		if(!attemptsRepo.existsById(quizAttempts_id))
			return 0;
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
	
	// Business
	
	
	public Boolean verifyFields(QuizAttempts entry) 
			throws BadRequestException
	{
		// Check if the entry's student/quiz/date is not null.
		if(entry.getStudent() == null)
			throw new BadRequestException("Entry is not related to a user and may not be related to a quiz either.");
		if (entry.getQuiz() == null)
			throw new BadRequestException("Entry is not related to a quiz.");
		if (entry.getAttempt_date() == null)
			throw new BadRequestException("Entry does not have a saved date.");
		
		// Ensure decimal has at most 2 decimal places by rounding:
		// Precision is injected via the pom under "org.apache.commons" "commons-math3"
		entry.setScore(Precision.round(entry.getScore(), 2));
			
		return true;
	}


}
