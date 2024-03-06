package com.example.tasksManagement.task;

import com.example.tasksManagement.task.taskEnum.TaskStatus;
import com.example.tasksManagement.user.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Long> {
    Optional<Task> findByTaskStatus(TaskStatus taskStatus);
    Optional<Task> findById(Long id);
}
