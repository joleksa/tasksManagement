package com.example.tasksManagement.task;

import com.example.tasksManagement.task.taskEnum.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Long>{
    List<Task> findListByTaskStatus(TaskStatus taskStatus);
    Optional<Task> findById(Long id);
    List<Task> findAll();
    @Query("SELECT t FROM Task t WHERE t.executionDate <= :warningDate AND t.executionDate > CURRENT_DATE ")
    List<Task> findWarnedTasks(LocalDateTime warningDate);

    @Query("SELECT t FROM Task t WHERE t.executionDate < CURRENT_DATE ")
    List<Task> findExpiredTasks(LocalDateTime expiredDate);

}
