package com.revature.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
public class MaximumAllowedQuizAttemptsException extends Exception{
	public MaximumAllowedQuizAttemptsException(String message) {
		super(message);
	}
}
