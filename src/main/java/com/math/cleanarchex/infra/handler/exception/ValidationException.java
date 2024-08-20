package com.math.cleanarchex.infra.handler.exception;

public class ValidationException extends RuntimeException {

    public ValidationException(String msg) {
        super(msg);
    }
}
