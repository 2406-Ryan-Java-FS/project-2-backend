package com.revature.exceptions;

import org.springframework.http.HttpStatus;

public class BadRequestException extends CustomHttpException {
    public BadRequestException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}