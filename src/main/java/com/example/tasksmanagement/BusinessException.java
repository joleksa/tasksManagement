package com.example.tasksmanagement;

public class BusinessException extends RuntimeException{
    public BusinessException(String message) {
        super(message);
    }
}
