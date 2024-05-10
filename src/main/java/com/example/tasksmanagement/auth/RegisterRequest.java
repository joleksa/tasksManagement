package com.example.tasksmanagement.auth;

import com.example.tasksmanagement.user.Role;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RegisterRequest {
    private String name;
    private String surname;
    private String login;
    private String password;
    private Role role;
}