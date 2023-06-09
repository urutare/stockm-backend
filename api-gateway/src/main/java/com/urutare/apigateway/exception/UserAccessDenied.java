package com.urutare.apigateway.exception;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class UserAccessDenied extends Exception {
    public UserAccessDenied(String message) {
        super(message);
    }
}
