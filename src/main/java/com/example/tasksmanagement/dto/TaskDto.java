package com.example.tasksmanagement.dto;

import com.example.tasksmanagement.task.taskEnum.TaskType;
import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public class TaskDto {
    private TaskType taskType;
    private String description;
    private Long assignedUserId;
    private Long createdById;
    private Integer daysToEnd;
}
