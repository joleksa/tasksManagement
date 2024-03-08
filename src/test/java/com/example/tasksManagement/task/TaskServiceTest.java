package com.example.tasksManagement.task;

import com.example.tasksManagement.Dto.TaskResponseDto;
import com.example.tasksManagement.task.taskEnum.TaskStatus;
import com.example.tasksManagement.task.taskEnum.TaskType;
import com.example.tasksManagement.user.AppUser;
import com.example.tasksManagement.user.AppUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.util.Assert;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class TaskServiceTest {

    private final TaskRepository taskRepository = Mockito.mock(TaskRepository.class);
    private final AppUserService appUserService = Mockito.mock(AppUserService.class);
    private final int expirationDaysWarning = 3;
    private TaskService taskService;

    @BeforeEach
    void setUp() {
        taskService = new TaskService(taskRepository, appUserService, expirationDaysWarning);
    }

    @Test
    void createTask() {
    }

    @Test
    void findTaskById() {
    }

    @Test
    void closeTask() {
    }

    @Test
    void should_change_task_status_to_in_progress() {
//        AppUser customUser = createCustomUser();
//        Task task = new Task(1L, TaskType.CREATE_MONTHLY_REPORT,"test", TaskStatus.NEW, customUser, customUser , LocalDateTime.now().minusDays(2), LocalDateTime.now().minusDays(2), LocalDateTime.now().plusDays(5));
//        taskService.inProgressTask(1L);
//        assertEquals(TaskStatus.IN_PROGRESS,task.getTaskStatus());
    }

    @Test
    void cancelTask() {
    }

    @Test
    void getAllTasks() {
    }

    @Test
    void assignTask() {
    }

    @Test
    void getWarnedTasks() {
    }

    @Test
    void should_return_all_expired_tasks() {
        List<Task> tasks = getTasksForMock(LocalDateTime.now().minusDays(1));
        when(taskRepository.findExpiredTasks(any())).thenReturn(tasks);
        List<TaskResponseDto> expiredTasks = taskService.getExpiredTasks();
        assertEquals(2,expiredTasks.size());
        assertTrue(LocalDateTime.now().isAfter(expiredTasks.get(0).getExecutionDate()));
    }

    private List<Task> getTasksForMock(LocalDateTime expirationDate) {
        AppUser customUser = createCustomUser();
        Task task = new Task(1L, TaskType.CREATE_MONTHLY_REPORT,"test", TaskStatus.NEW, customUser, customUser , LocalDateTime.now().minusDays(2), LocalDateTime.now().minusDays(2), expirationDate);
        Task task2 = new Task(2L, TaskType.CREATE_MONTHLY_REPORT,"test2", TaskStatus.NEW, customUser, customUser , LocalDateTime.now().minusDays(2), LocalDateTime.now().minusDays(2), expirationDate);
        return List.of(task, task2);
    }

    private AppUser createCustomUser() {
        return new AppUser( "Adam", "Klocek", "akloc");
    }
}