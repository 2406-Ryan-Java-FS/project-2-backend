package com.revature.exceptions;

import org.springframework.http.HttpStatus;

public class ConflictException extends CustomHttpException {
    public ConflictException(String message) {
        super(message, HttpStatus.CONFLICT);
    }
}
