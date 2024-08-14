package com.personaltask.schedulemanagement.domain.schedule.errors;


public class ResponseError extends RuntimeException{

    private String status;
    private RuntimeException e;

    public ResponseError(String status, RuntimeException e) {
        this.status = status;
        this.e = e;
    }
}
