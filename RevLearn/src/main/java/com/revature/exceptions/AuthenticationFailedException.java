package com.revature.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


public class AuthenticationFailedException extends CustomHttpException{
    public AuthenticationFailedException(String message){
        super(message,HttpStatus.UNAUTHORIZED);
    }
}
