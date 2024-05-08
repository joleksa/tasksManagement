package com.example.tasksmanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AppUserDto {
    private Long id;
    private String name;
    private String surname;
    private String login;
}
