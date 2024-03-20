package com.example.tasksManagement.Dto;

import com.example.tasksManagement.task.taskEnum.TaskType;
import lombok.Getter;


@Getter
public class TaskDto {
    private TaskType taskType;
    private String description;
    private Long assignedUserId;
    private Long createdById;
    private Integer daysToEnd;
}
