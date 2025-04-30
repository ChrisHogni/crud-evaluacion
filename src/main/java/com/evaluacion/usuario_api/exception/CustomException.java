package com.evaluacion.usuario_api.exception;


public class CustomException extends RuntimeException {
    public CustomException(String message) {
        super(message);
    }
}