package org.example.testetecniconimble.exception;

import lombok.Getter;

@Getter
public class ErrorResposta {
    private final int status;
    private final String error;
    private final String message;

    public ErrorResposta(int status, String error, String message) {
        this.status = status;
        this.error = error;
        this.message = message;
    }


}
