package com.revature.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public abstract class CustomHttpException extends RuntimeException {

    private final HttpStatus status;

    protected CustomHttpException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }
}
