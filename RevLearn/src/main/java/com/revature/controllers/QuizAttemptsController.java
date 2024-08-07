// Steven Ray Coronel

package com.revature.controllers;

import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.HashSet;
//Core Libraries
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.math3.util.Precision;
//Spring
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

import com.revature.models.ChoiceSelection;
import com.revature.models.QuestionChoice;
//Local
import com.revature.models.QuizAttempts;
import com.revature.models.QuizQuestion;
import com.revature.models.User;
import com.revature.models.dtos.QuizAttemptsDTO;
import com.revature.models.dtos.QuizAttemptsEditDTO;
import com.revature.services.ChoiceSelectionService;
import com.revature.services.ChoiceSelectionServiceImpl;
import com.revature.services.JwtServiceImpl;
import com.revature.services.QuestionChoiceServiceImpl;
import com.revature.services.QuizAttemptsServiceImpl;
import com.revature.services.QuizQuestionServiceImpl;
import com.revature.exceptions.*;


//-------------CRUD-----------------//
//
// POST   on /newQuizAttempts
// GET    on /quizAttempts
//           /quizAttempts/{quizAttempt_id}
// PATCH  on /quizAttempts/{quizAttempt_id}
// DELETE on /quizAttempts/{quizAttempt_id}

//---------Quiz-based CRUD----------//
//
// GET    on /quizAttemptsFromQuiz/{quiz_id}
// DELETE on /quizAttemptsFromQuiz/{quiz_id}

//-----Student-based Operations-----//
//
// GET    on /quizAttemptsFromUser/{user_id}
// DELETE on /quizAttemptsFromUser/{user_id}

//---Quiz&Student-based Operations--//
//
// GET    on /quizAttemptsFromQuizAndUser
// DELETE on /quizAttemptsFromQuizAndUser

//------Course-based Operations-----//
//GET    on /quizAttemptsFromCourse/{course_id}
//DELETE on /quizAttemptsFromCourse/{course_id}

// if any of this is inconsistent in terms of what is present or working
//    in this controller, then please let Steven Ray Coronel know.

@Validated
@RestController

//@CrossOrigin
public class QuizAttemptsController {

	QuizAttemptsServiceImpl attemptsServ;
	JwtServiceImpl jwtServ;
	ChoiceSelectionServiceImpl selectionServ;
	QuestionChoiceServiceImpl optionServ;
	QuizQuestionServiceImpl questionServ;
	
	@Autowired
	QuizAttemptsController(QuizAttemptsServiceImpl attemptsServ, 
			JwtServiceImpl jwtServ, 
			ChoiceSelectionServiceImpl selectionServ,  
			QuestionChoiceServiceImpl optionServ,
			QuizQuestionServiceImpl questionServ)
	{
		this.attemptsServ = attemptsServ;
		this.jwtServ = jwtServ;
		this.selectionServ = selectionServ;
		this.optionServ = optionServ;
		this.questionServ = questionServ;
	}	
	
	////////////////////////////
	//---------Create---------//
	////////////////////////////
	
	// Create new entry
	// Receives a QuizAttemptDTO that MAY have a Timestamp, but won't have an id. 
	// - A student can only create for themselves.
	// - An educator can create freely.
	// - An attempt cannot exceed the maximum allowed for a given quiz
	@PostMapping(value = "/newQuizAttempts")    
	@ResponseStatus( HttpStatus.CREATED)
	public @ResponseBody  QuizAttempts createQuizAttempts(@RequestBody QuizAttemptsDTO newEntry, @RequestHeader(name = "Authorization") String token)
	throws BadRequestException, UnauthorizedException, MaximumAllowedQuizAttemptsException
	{
		// Extract a user from the token
		User user = jwtServ.getUserFromToken(token);
				
		// If user is a student...
		if(user.getRole().name().equals("student"))
		{
			// and the user is making an entry for themselves...
			if(newEntry.getStudent_id() != user.getUserId())
				throw new UnauthorizedException("User doesn't match user id in new data");

			// ... then we create an entry.
			return attemptsServ.create(newEntry);
		}
		
		// Otherwise, the user is an educator, who can create a QuizAttempts without restriction.			
		if(user.getRole().name().equals("educator"))
			return attemptsServ.create(newEntry);
		
		// otherwise, we do not recognize the user.
		throw new UnauthorizedException("User is not recognized, abandoning createQuizAttempts.");
	}
	
	////////////////////////////
	//--------Retrieve--------//
	////////////////////////////
	
	// Get all quizAttempts entries.
	// - Only an educator can view all attempts.
	@GetMapping(value = "/quizAttempts")
    public @ResponseBody List<QuizAttempts> getAllQuizAttempts(@RequestHeader(name = "Authorization") String token)
    	throws UnauthorizedException
    {	
		// Extract a user from the token
		User user = jwtServ.getUserFromToken(token);
		
		// If the User is an educator...
		if(!user.getRole().name().equals("educator"))
			throw new UnauthorizedException("User is not an educator, abandoning getAllQuizAttempts.");
		
		// ... then proceed with retrieving all QuizAttempts.
        return attemptsServ.getAll();
    }
	
	// Get one QuizAttempts via its id.
	// Receives a QuizAttempts ID to search with.
	// - A student can view their own QiuzAttempts.
	// - An educator can freely view any QuizAttempts.
	@GetMapping(value = "/quizAttempts/{quizAttempts_id}")
    public @ResponseBody QuizAttempts getQuizAttemptById(@PathVariable Integer quizAttempts_id, @RequestHeader(name = "Authorization") String token) 
    		throws BadRequestException, UnauthorizedException
    {	
		// Extract a user from the token
		User user = jwtServ.getUserFromToken(token);
		
		// If the user is a student...
		if(user.getRole().name().equals("student"))
		{	
			QuizAttempts quizAttempts = attemptsServ.getById(quizAttempts_id);
			
			// and is requesting their own data...
			if(quizAttempts.getStudent().getUserId() != user.getUserId())
				throw new UnauthorizedException("Requested QuizAttempts does not belong to user.");
				
			// ...then we can retrieve the entry.
			return quizAttempts;
		}
			
		// if the user is an educator... then proceed.
		if(user.getRole().name().equals("educator"))
			return attemptsServ.getById(quizAttempts_id);
			
		// otherwise, we do not recognize the user. 
		throw new UnauthorizedException("User is not an educator, abandoning getQuizAttemptsById.");
    }
	
	// Get all quizAttempts for a Quiz via that Quiz's Id.
    // Receives a Quiz Id to search with.
    // - Only educator can retrieve all QuizAttempts for a Quiz
	@GetMapping(value = "/quizAttemptsFromQuiz/{quiz_id}")
    public @ResponseBody List<QuizAttempts> getAllQuizAttemptsByQuizId(@PathVariable Integer quiz_id, @RequestHeader(name = "Authorization") String token) 
    		throws BadRequestException, UnauthorizedException
    {	
    	// Extract a User from the token.
		User user = jwtServ.getUserFromToken(token);
		
		// If the user is an educator...
		if(!user.getRole().name().equals("educator"))
			throw new UnauthorizedException("Educator level access required, abandoning getAllQuizAttemptsByQuizId.");
        
		// ...then proceed with retrieval.
		return attemptsServ.getAllByQuizId(quiz_id);
    }
	
    // Gets all QuizAttempts for a User by that User's Id.
    // Receives a User Id to search with.
    // - A student can view all of their own QuizAttempts.
    // - An educator can view any QuizAttempts.
	@GetMapping(value = "/quizAttemptsFromUser/{user_id}")
    public @ResponseBody List<QuizAttempts> getQuizAttemptByStudentId(@PathVariable Integer user_id, @RequestHeader(name = "Authorization") String token) 
    		throws BadRequestException, UnauthorizedException
    {	
		// Extract a user from the token
		User user = jwtServ.getUserFromToken(token);
		
		// If the user is a student...
		if(user.getRole().name().equals("student"))
		{	
			// and is requesting their own data...
			if(user.getUserId() != user_id)
				throw new UnauthorizedException("Requested QuizAttempts does not belong to user.");
				
			// ... then we can retrieve the entries.
			return attemptsServ.getAllByStudentId(user_id);
		}	
		
		// if the user is an educator... then proceed.
		if(user.getRole().name().equals("educator"))
			return attemptsServ.getAllByStudentId(user_id);
			
		// otherwise, we do not recognize the user. 
		throw new UnauthorizedException("User is not an educator, abandoning getAllQuizAttemptsByStudentId.");
    }
	
    // Gets all QuizAttempts for a User by that User's Id.
    // Receives a User Id to search with.
    // - A student can view all of their own QuizAttempts.
    // - An educator can view any QuizAttempts.
	@GetMapping(value = "/quizAttemptsFromCourse/{course_id}")
    public @ResponseBody List<QuizAttempts> getQuizAttemptByCourseId(@PathVariable Integer course_id, @RequestHeader(name = "Authorization") String token) 
    		throws BadRequestException, UnauthorizedException
    {	
		// Extract a user from the token
		User user = jwtServ.getUserFromToken(token);
		
		// if the user is an educator... then proceed.
		if(!user.getRole().name().equals("educator"))
			throw new UnauthorizedException("Only Educators can see all quizes by CourseId.");

			
		return attemptsServ.getAllByCourseId(course_id);
    }
	
	// Retrieving all attempts for a User and Quiz
    // - "Retrieve a all of a user's attempts on a quiz".
    // Get a student id and quiz id to search with.
    // - A student can view their own QuizAttempts.
    // - An educator can retrieve this data.
	@GetMapping(value = "/quizAttemptsFromQuizAndUser")
    public @ResponseBody List<QuizAttempts> getQuizAttemptByQuizAndStudent(@RequestParam("quizId") Integer quiz_id,  @RequestParam("userId") Integer user_id, @RequestHeader(name = "Authorization") String token) 
    		throws BadRequestException, UnauthorizedException
    {	
		// Extract a user from the token
		User user = jwtServ.getUserFromToken(token);
		
		// If the user is a student...
		if(user.getRole().name().equals("student"))
		{	
			// and is requesting their own data...
			if(user.getUserId() != user_id)
				throw new UnauthorizedException("Requested QuizAttempts does not belong to user.");
				
			// ...then we can retrieve the entries.
			return attemptsServ.getAllByStudentIdAndQuizId(quiz_id, user_id);
		}	
		
		// if the user is an educator... then proceed.
		if(user.getRole().name().equals("educator"))
			return attemptsServ.getAllByStudentIdAndQuizId(quiz_id, user_id);
			
		// otherwise, we do not recognize the user. 
		throw new UnauthorizedException("User is not an educator, abandoning getAllQuizAttemptsByStudentId.");
    }
    
	
	////////////////////////////
	//---------Update---------//
	////////////////////////////
	
	// Update a QuizAttempts via its id.
	// Receives a QuizAttempts ID to search with, and a QuizAttemptsDTO.
	// - only an educator can update a QuizAttempts
	// - will not change attempt_date if the incoming DTO's attempt_date is null
	// - may result in exception if switching an attempt's quiz id, \
	//   which can exceed max attempts.
    @PatchMapping(value = "/quizAttempts/{quizAttempts_id}")
    public @ResponseBody QuizAttempts updateQuizAttemptsById(@PathVariable Integer quizAttempts_id, @RequestBody QuizAttemptsEditDTO newData, @RequestHeader(name = "Authorization") String token) 
    throws BadRequestException, UnauthorizedException, MaximumAllowedQuizAttemptsException
    {
    	// Extract a User from the token.
		// User user = jwtServ.getUserFromToken(token);
		
		
		/* Security issue: students can declare their own score if score is submitted from 
		   the front end.
		   Solution: score should be calculated on the backend and a request to process 
		   such a score should be commanded via an endpoint.
		// If user is an educator...
		if(user.getRole().name().equals("educator"))
			throw new UnauthorizedException("Educator level access required, abandoning updateQuizAttemptsById.");
		*/

		// ...then proceed, but keep original timestamp if there is no new one.
    	if(newData.getAttempt_date() == null)
    		return attemptsServ.updateByIdNoTime(quizAttempts_id, newData);
    	else
    		return attemptsServ.updateByIdWithTime(quizAttempts_id, newData);
    }
    
	////////////////////////////
	//---------Delete---------// 
	////////////////////////////
    
    // NOTE: unsure how to make use of cascade logic yet. //
    
	// Deletes a QuizAttepts by its Id
    // Receives a quizAttempts Id to search with.
    // - only an educator delete quizAttempts.
    @DeleteMapping(value = "/quizAttempts/{quizAttempts_id}") 
    @ResponseStatus( HttpStatus.NO_CONTENT )
    public @ResponseBody Integer deleteQuizAttemptsById(@PathVariable Integer quizAttempts_id, @RequestHeader(name = "Authorization") String token)
    throws UnauthorizedException
    {
    	// Extract a User from the token.
		User user = jwtServ.getUserFromToken(token);
		
		// If the user is an educator...
		if(!user.getRole().name().equals("educator"))
			throw new UnauthorizedException("Educator level access required, abandoning deleteQuizAttemptsById.");

		// ...then proceed with deleting the QuizAttempts.
        return attemptsServ.deleteById(quizAttempts_id);
    }
    
    // Delete all QuizAttempts for a Quiz by that Quiz's Id.
    // Receives a Quiz Id to search with.
    // - Only an educator delete all QuizAttempts for a Quiz.
    @DeleteMapping(value = "/quizAttemptsFromQuiz/{quiz_id}") 
    @ResponseStatus( HttpStatus.NO_CONTENT )
    public @ResponseBody Integer deleteQuizAttemptsByQuizId(@PathVariable Integer quiz_id, @RequestHeader(name = "Authorization") String token)
    throws UnauthorizedException
    {
    	// Extract a User from the token.
		User user = jwtServ.getUserFromToken(token);
		
		// If the user is an educator...
		if(!user.getRole().name().equals("educator"))
			throw new UnauthorizedException("Educator level access required, abandoning deleteQuizAttemptsByQuizId.");

		// ...then proceed with deleting the QuizAttempts.
        return attemptsServ.deleteAllByQuizId(quiz_id);
    }

	// Delete all QuizAttempts for a User by that User's Id
    // Receive a User Id to search with.
    @DeleteMapping(value = "/quizAttemptsFromUser/{user_id}") 
    @ResponseStatus( HttpStatus.NO_CONTENT )
    public @ResponseBody Integer deleteQuizAttemptsByStudentId(@PathVariable Integer user_id, @RequestHeader(name = "Authorization") String token)
    throws UnauthorizedException
    {
    	// Extract a User from the token.
		User user = jwtServ.getUserFromToken(token);
		
		// If the user is an educator...
		if(!user.getRole().name().equals("educator"))
			throw new UnauthorizedException("Educator level access required, abandoning deleteQuizAttemptsByStudentId.");

		// ...then proceed with deleting the QuizAttempts.
        return attemptsServ.deleteAllByStudentId(user_id);
    }
    
	// Delete all QuizAttempts for a User by that User's Id
    // Receive a User Id to search with.
    @DeleteMapping(value = "/quizAttemptsFromCourse/{course_id}") 
    @ResponseStatus( HttpStatus.NO_CONTENT )
    public @ResponseBody Integer deleteQuizAttemptsByCourseId(@PathVariable Integer course_id, @RequestHeader(name = "Authorization") String token)
    throws UnauthorizedException
    {
    	// Extract a User from the token.
		User user = jwtServ.getUserFromToken(token);
		
		// If the user is an educator...
		if(!user.getRole().name().equals("educator"))
			throw new UnauthorizedException("Educator level access required, abandoning deleteQuizAttemptsByStudentId.");

		// ...then proceed with deleting the QuizAttempts.
        return attemptsServ.deleteAllByCourseId(course_id);
    }
    
	// Delete all QuizAttempts for a student's quiz via QuizId and UserId
    // Receive a User Id and quiz Id to search with. note: not by path.
    @DeleteMapping(value = "/quizAttemptsFromQuizAndUser") 
    @ResponseStatus( HttpStatus.NO_CONTENT )
    public @ResponseBody Integer deleteQuizAttemptsByQuizAndStudent(@RequestParam("quizId") Integer quiz_id,  @RequestParam("userId") Integer user_id, @RequestHeader(name = "Authorization") String token)
    throws UnauthorizedException
    {
    	// Extract a User from the token.
		User user = jwtServ.getUserFromToken(token);
		
		// If the user is an educator...
		if(!user.getRole().name().equals("educator"))
			throw new UnauthorizedException("Educator level access required, abandoning deleteQuizAttemptsByStudentId.");

		// ...then proceed with deleting the QuizAttempts.
        return attemptsServ.deleteAllByQuizAndStudent(quiz_id, user_id);
    }
    
    // Business

	// Calculate score based on ChoiceSelection entries and the relevant question's correct fields.
    // Since this is a command to process internal data, there isn't security danger to letting this be accessed
    // aside from DDOS?
    // - Essentially, JWT will not be relevant here.
    @PutMapping(value = "/quizAttempts/{quizAttempt_id}/score") 
    public @ResponseBody Double getScore(@PathVariable("quizAttempt_Id") Integer quizAttempt_id, @RequestHeader(name = "Authorization") String token)
    {
    	// return value to be sent later.
    	Double score = 0.0;
    	
    	// keeps track of unique questions. if the size isn't the same as the choices,
    	// then we have a unique situation.
    	Set<QuizQuestion> uniqueQuestions = new HashSet<QuizQuestion>();
    	
    	// selections from quizAttempt
    	List<ChoiceSelection> selections = selectionServ.getAttemptSelections(quizAttempt_id);
    	  
    	// to make the nested code more pretty.
    	QuestionChoice currentChoice;
    	
    	// Go through all choices and increase score for each correct answer.
    	// Also add to uniqueQuestions to be able to keep track of multiple answers for
    	// later implementation.
    	for(int i=0; i < selections.size(); i++)
    	{
    		currentChoice = optionServ.getChoice(selections.get(i).getChoiceId());
    		uniqueQuestions.add(questionServ.getQuestion(currentChoice.getQuestionId()));
    		if(optionServ.getChoice(selections.get(i).getChoiceId()).isCorrect())
    			score++;
    	}
    	
    	// no unique questions, no score! plus lets not divide by 0.
    	if(uniqueQuestions.size() == 0)
    	{
    		return 0.0;
    	}
    	else
    	{
        	// Now we divide by the total unique questions
        	score = score / uniqueQuestions.size();
    	}

    	// it is possible to account for an inconsistency between 
    	//   number of selections vs unique questions, but we haven't set
    	//   every rule yet.
    	
		return Precision.round(score, 2);
    }

    
	// For anything more past the comments above, Please Ask Steven Coronel
    // note: I need to study for the upcoming assessments.
		
}
