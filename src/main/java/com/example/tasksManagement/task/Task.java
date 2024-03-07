package com.example.tasksManagement.task;


import com.example.tasksManagement.task.taskEnum.TaskStatus;
import com.example.tasksManagement.task.taskEnum.TaskType;
import com.example.tasksManagement.user.AppUser;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;


@Entity
@Table(name = "TASKS")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    private TaskType taskType;
    private String description;
    @Enumerated(EnumType.STRING)
    private TaskStatus taskStatus;
    @ManyToOne
    @JoinColumn(name = "created_by")
    private AppUser createdBy;
    @ManyToOne
    @JoinColumn(name = "assigned_user")
    private AppUser assignedUser;
    private LocalDateTime creationDate;
    private LocalDateTime modificationDate;
    private LocalDateTime executionDate;

    public Task(TaskType taskType,
                String description,
                AppUser assignedUser,
                AppUser createdBy,
                int daysToEnd) {
        this.taskType = taskType;
        this.description = description;
        this.taskStatus = TaskStatus.NEW;
        this.assignedUser = assignedUser;
        this.createdBy = createdBy;
        this.executionDate = LocalDateTime.now().plusDays(daysToEnd);
        this.creationDate = LocalDateTime.now();
        this.modificationDate = LocalDateTime.now();
    }

    public Task(TaskType taskType, String description, AppUser createdBy, AppUser assignedUser) {
        this.taskType = taskType;
        this.description = description;
        this.taskStatus = TaskStatus.NEW;
        this.createdBy = createdBy;
        this.assignedUser = assignedUser;
        this.creationDate = LocalDateTime.now();
        this.modificationDate = LocalDateTime.now();
    }
}
