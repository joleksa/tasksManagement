package com.example.tasksmanagement.auth;

import lombok.Data;

@Data
public class RegisterRequest {
    private String name;
    private String surname;
    private String login;
    private String password;
}