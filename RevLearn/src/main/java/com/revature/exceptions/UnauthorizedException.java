package com.revature.exceptions;

import org.springframework.http.HttpStatus;

public class UnauthorizedException extends CustomHttpException {
    public UnauthorizedException(String message) {
        super(message, HttpStatus.UNAUTHORIZED);
    }
}