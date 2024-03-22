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
@AllArgsConstructor
@Builder
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

    public void setOptionalExecutionDate(Integer daysToEnd) {
        if (daysToEnd != null) {
            this.executionDate = LocalDateTime.now().plusDays(daysToEnd);
        }
    }
}
