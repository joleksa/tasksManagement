package com.example.tasksmanagement.dto;

import com.example.tasksmanagement.task.taskEnum.TaskStatus;
import com.example.tasksmanagement.task.taskEnum.TaskType;
import jakarta.annotation.Nullable;

import java.time.LocalDateTime;

public record TaskFilterDto(TaskStatus taskStatus,
                            TaskType taskType,
                            Long assignedUserId,
                            @Nullable LocalDateTime startDate,
                            @Nullable LocalDateTime endDate) {


    public TaskFilterDto(TaskType taskType) {
        this(null, taskType, null, null, null);
    }

    public TaskFilterDto(LocalDateTime startDate, LocalDateTime endDate) {
        this(null, null, null, startDate, endDate);
    }
}

