package com.example.tasksmanagement.dto;

import com.example.tasksmanagement.task.taskEnum.TaskStatus;
import com.example.tasksmanagement.task.taskEnum.TaskType;
import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class TaskFilterDto {

    private TaskStatus taskStatus;
    private TaskType taskType;
    private Long assignedUserId;
    @Nullable
    private LocalDateTime startDate;
    @Nullable
    private LocalDateTime endDate;

    public TaskFilterDto(TaskType taskType) {
        this.taskType = taskType;
    }
    public TaskFilterDto(LocalDateTime startDate, LocalDateTime endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }
}

