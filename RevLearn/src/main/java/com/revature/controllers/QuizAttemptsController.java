// Steven Ray Coronel

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
import com.revature.models.User;
import com.revature.repositories.UserRepository;
import com.revature.services.JwtServiceImpl;
import com.revature.services.QuizAttemptsServiceImpl;
import com.revature.services.UserService;
import com.revature.DTO.QuizAttemptsDTO;
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
	
	@Autowired
	JwtServiceImpl jwtServ;
	
	// for authentication. This may be later updated to UserServiceImpl, but
	//		UserServiceImpl currently doesn't exist on my system. Only UserService.
	@Autowired
	UserRepository userServ;
	
	////////////////////////////
	//---------Create---------//
	////////////////////////////
	
	/// Create new entry
	@PostMapping(value = "/newQuizAttempts")    
	@ResponseStatus( HttpStatus.CREATED)
	public @ResponseBody  QuizAttempts createQuizAttempts(@RequestBody QuizAttemptsDTO newEntry, @RequestHeader(name = "Authorization") String token)
	throws BadRequestException, UnauthorizedException
	{
		User user = jwtServ.getUserFromToken(token);
		if(!user.getRole().name().equals("educator"))
			throw new UnauthorizedException("Educator level access required to create a quiz attempt.");

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
    public @ResponseBody QuizAttempts updateQuizAttempts(@PathVariable Integer quizAttempts_id, @RequestBody QuizAttemptsDTO newData, @RequestHeader(name = "Authorization") String token) 
    throws BadRequestException, UnauthorizedException
    {
		User user = jwtServ.getUserFromToken(token);
		if(!user.getRole().name().equals("educator"))
			throw new UnauthorizedException("Educator level access required to create a quiz attempt.");

    	if(newData.getAttempt_date() == null)
    		return attemptsServ.updateByIdNoTime(quizAttempts_id, newData);
    	else
    		return attemptsServ.updateByIdWithTime(quizAttempts_id, newData);
    }
    
	////////////////////////////
	//---------Delete---------//
	////////////////////////////
	
    @DeleteMapping(value = "/quizAttempts/{quizAttempts_id}") 
    @ResponseStatus( HttpStatus.NO_CONTENT )
    public @ResponseBody Integer deleteQuizAttemptsById(@PathVariable Integer quizAttempts_id, @RequestHeader(name = "Authorization") String token)
    throws UnauthorizedException
    {
		User user = jwtServ.getUserFromToken(token);
		if(!user.getRole().name().equals("educator"))
			throw new UnauthorizedException("Educator level access required to create a quiz attempt.");

        return attemptsServ.deleteById(quizAttempts_id);
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
	
	// For anything more past the comments above, Please Ask Steven Coronel
	

	
}
