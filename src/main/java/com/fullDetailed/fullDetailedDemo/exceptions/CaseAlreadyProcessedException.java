package com.fullDetailed.fullDetailedDemo.exceptions;

public class CaseAlreadyProcessedException extends RuntimeException {
    public CaseAlreadyProcessedException(String message) {
        super(message);
    }
}