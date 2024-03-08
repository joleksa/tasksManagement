package com.example.tasksManagement.Dto;

import com.example.tasksManagement.task.taskEnum.TaskStatus;
import com.example.tasksManagement.task.taskEnum.TaskType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class TaskResponseDto {
    private TaskType taskType;
    private String description;
    private TaskStatus taskStatus;
    private LocalDateTime executionDate;
}
