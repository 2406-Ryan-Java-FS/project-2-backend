package com.revature.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class ConflictException extends CustomHttpException {
    public ConflictException(String message) {
        super(message, HttpStatus.CONFLICT);
    }
}
