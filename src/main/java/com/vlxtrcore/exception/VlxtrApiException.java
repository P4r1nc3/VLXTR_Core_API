package com.vlxtrcore.exception;

import org.springframework.http.HttpStatus;

public class VlxtrApiException extends RuntimeException {
    private final HttpStatus status;
    private final String cause;
    private final String action;

    public VlxtrApiException(HttpStatus status, String message, String cause, String action) {
        super(message);
        this.status = status;
        this.cause = cause;
        this.action = action;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getCauseMessage() {
        return cause;
    }

    public String getAction() {
        return action;
    }
}
