package com.example.tasksmanagement.dto;

import com.example.tasksmanagement.task.taskEnum.TaskStatus;
import com.example.tasksmanagement.task.taskEnum.TaskType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class TaskResponseDto {
    private TaskType taskType;
    private String description;
    private TaskStatus taskStatus;
    private LocalDateTime executionDate;
}
