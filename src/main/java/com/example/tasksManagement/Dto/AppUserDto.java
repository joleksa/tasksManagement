package com.example.tasksManagement.Dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AppUserDto {
    private String name;
    private String surname;
    private String login;
}
