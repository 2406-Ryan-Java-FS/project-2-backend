package com.revature.exceptions;

import org.springframework.http.HttpStatus;

public class InternalServerErrorException extends CustomHttpException {
    public InternalServerErrorException(String message) {
        super(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
