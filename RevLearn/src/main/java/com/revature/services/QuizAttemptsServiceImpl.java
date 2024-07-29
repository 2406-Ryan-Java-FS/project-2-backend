package com.revature.services;

import java.util.List;

import org.apache.commons.math3.util.Precision;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

// Local
import com.revature.models.QuizAttempts;
import com.revature.models.dtos.QuizAttemptsDTO;
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
	
	///////-------Basic CRUD--------///////
	
	// Create an entry
	public QuizAttempts create(QuizAttemptsDTO newDTOEntry) 
			throws com.revature.exceptions.BadRequestException
	{		
		QuizAttempts newEntity = verifyFields(newDTOEntry, 0);
		
		return attemptsRepo.save(newEntity);
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
			throw new BadRequestException("QuizAttemptsService getById - entry with given id doesn't exist.");
		return attemptsRepo.findById(quizAttempts_id).get();
	}

	// Update Entries by id, this assumes we do not update the time.
	public QuizAttempts updateByIdNoTime(int quizAttempts_id, QuizAttemptsDTO newData) 
			throws BadRequestException
	{
		
		// This is update, not create. Only perform if the entry with id exists.
		if(!attemptsRepo.existsById(quizAttempts_id))
			throw new BadRequestException("QuizAttemptsService updateById - entry with given id doesn't exist.");
		
		// This method assumes that time will not be updated, so we'll use the current one.
		newData.setAttempt_date(attemptsRepo.findById(quizAttempts_id).get().getAttempt_date());
		
		QuizAttempts updatedEntity = verifyFields(newData, quizAttempts_id);
						
		return attemptsRepo.save(updatedEntity);
	}
	
	// Update Entries by id, this assumes we do not update the time.
	public QuizAttempts updateByIdWithTime(int quizAttempts_id, QuizAttemptsDTO newData) 
			throws BadRequestException
	{
		
		// This is update, not create. Only perform if the entry with id exists.
		if(!attemptsRepo.existsById(quizAttempts_id))
			throw new BadRequestException("QuizAttemptsService updateById - entry with given id doesn't exist.");
				
		 
		QuizAttempts updatedEntity = verifyFields(newData, quizAttempts_id);
						
		return attemptsRepo.save(updatedEntity);
	}

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

}
