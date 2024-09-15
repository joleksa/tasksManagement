package com.example.tasksmanagement.dto;

import com.example.tasksmanagement.task.taskEnum.TaskType;


public record TaskDto(
         TaskType taskType,
         String description,
         Long assignedUserId,
         Long createdById,
         Integer daysToEnd
) {

}
