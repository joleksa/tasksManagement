package com.example.tasksManagement;

public class BusinessException extends RuntimeException{
    public BusinessException(String message) {
        super(message);
    }
}
