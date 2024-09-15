package com.example.tasksmanagement.dto;

import com.example.tasksmanagement.task.taskEnum.TaskStatus;
import com.example.tasksmanagement.task.taskEnum.TaskType;


import java.time.LocalDateTime;


public record TaskResponseDto(TaskType taskType,
                              String description,
                              TaskStatus taskStatus,
                              LocalDateTime executionDate) {

}
