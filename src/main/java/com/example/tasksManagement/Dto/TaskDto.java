package com.example.tasksManagement.Dto;

import com.example.tasksManagement.task.taskEnum.TaskStatus;
import com.example.tasksManagement.task.taskEnum.TaskType;
import com.example.tasksManagement.user.AppUser;
import lombok.Getter;


@Getter
public class TaskDto {
    private TaskType taskType;
    private String description;
    private Long assignedUserId;
    private Long createdById;
    private int daysToEnd;
}
