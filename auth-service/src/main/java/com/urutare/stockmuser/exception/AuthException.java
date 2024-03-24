package com.urutare.stockmuser.exception;

import com.urutare.stockmuser.models.Error;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class AuthException extends RuntimeException {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private Error error;

    public AuthException(String message) {
        super(message);
    }

    public AuthException(String message, Error error) {
        super(message);
        this.error = error;
    }

    public Error getError() {
        return error;
    }
}
