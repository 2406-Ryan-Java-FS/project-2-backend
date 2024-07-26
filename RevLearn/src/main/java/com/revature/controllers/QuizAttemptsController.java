package com.revature.controllers;

//Core Libraries
import java.util.List;


//Spring
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

//Local
import com.revature.models.QuizAttempts;
import com.revature.services.QuizAttemptsServiceImpl;
import com.revature.exceptions.*;

//-----------------CRUD-----------------//
//
// POST   on /newQuizAttempts
// GET    on /quizAttempts
//           /quizAttempts/{quizAttempt_id}
// PATCH  on /quizAttempts/{quizAttempt_id}
// DELETE on /quizAttempts/{quizAttempt_id}

@Validated
@RestController
public class QuizAttemptsController {

	@Autowired
	QuizAttemptsServiceImpl attemptsServ;
	

	
	////////////////////////////
	//---------Create---------//
	////////////////////////////
	
	/// Create new entry
	@PostMapping(value = "/newQuizAttempts")    
	@ResponseStatus( HttpStatus.CREATED)
	public @ResponseBody  QuizAttempts createQuizAttempts(@RequestBody QuizAttempts newEntry) //, @RequestHeader(name = "Authorization") String token)
	throws BadRequestException//, UnauthorizedUserException
	{
		
        // REQUIRES AUTHENTICATION
		
		return attemptsServ.create(newEntry);
	}
	
	////////////////////////////
	//--------Retrieve--------//
	////////////////////////////
	
	// get all quizAttempts entries
	@GetMapping(value = "/quizAttempts")
    public @ResponseBody List<QuizAttempts> getAllQuizAttempts()
    {	
        return attemptsServ.getAll();
    }
	
	// get one quizAttempts via quizAttempts id
	@GetMapping(value = "/quizAttempts/{quizAttempts_id}")
    public @ResponseBody QuizAttempts getFoodById(@PathVariable Integer quizAttempts_id) 
    		throws BadRequestException
    {	
        return attemptsServ.getById(quizAttempts_id);
    }
	////////////////////////////
	//---------Update---------//
	////////////////////////////
	
    @PatchMapping(value = "/quizAttempts/{quizAttempts_id}")
    public @ResponseBody QuizAttempts updateQuizAttempts(@PathVariable Integer quizAttempts_id, @RequestBody QuizAttempts newData)//, @RequestHeader(name = "Authorization") String token) 
    throws BadRequestException//, UnauthorizedUserException
    {
    	// REQUIRES AUTHENTICATION

        return attemptsServ.updateById(quizAttempts_id, newData);
    }
    
	////////////////////////////
	//---------Delete---------//
	////////////////////////////
	
    @DeleteMapping(value = "/quizAttempts/{quizAttempts_id}") 
    @ResponseStatus( HttpStatus.NO_CONTENT )
    public @ResponseBody Integer deleteQuizAttemptsById(@PathVariable Integer quizAttemp_id)//, @RequestHeader(name = "Authorization") String token)
    //throws UnauthorizedUserException
    {
    	//  REQUIRES AUTHENTICATION

        return attemptsServ.deleteById(quizAttemp_id);
    }
    
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
