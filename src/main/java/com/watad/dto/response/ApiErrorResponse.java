package com.watad.dto.response;

import java.time.LocalDateTime;

public class ApiErrorResponse {

    private String error ;
    private String message;
    private int status ;
    private LocalDateTime timeStamp ;


    public ApiErrorResponse() {
        this.timeStamp = LocalDateTime.now();
    }

    public ApiErrorResponse(String error, String message, int status) {
        this();
        this.error = error;
        this.message = message;
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public LocalDateTime getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(LocalDateTime timeStamp) {
        this.timeStamp = timeStamp;
    }
}
