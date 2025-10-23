package org.example.testetecniconimble.exception;

public class AutorizadorExternoException extends Exception {
    public AutorizadorExternoException(String message) {
        super(message);
    }
    public AutorizadorExternoException(String message, Throwable cause) {
        super(message, cause);
    }
}