package com.example.tasksManagement.task;

import com.example.tasksManagement.BusinessException;
import com.example.tasksManagement.Dto.TaskResponseDto;
import com.example.tasksManagement.task.taskEnum.TaskStatus;
import com.example.tasksManagement.task.taskEnum.TaskType;
import com.example.tasksManagement.user.AppUser;
import com.example.tasksManagement.user.AppUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.util.Assert;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class TaskServiceTest {

    private final TaskRepository taskRepository = Mockito.mock(TaskRepository.class);
    private final AppUserService appUserService = Mockito.mock(AppUserService.class);
    private TaskFilterService taskFilterService;
    private final int expirationDaysWarning = 3;
    private TaskService taskService;

    @BeforeEach
    void setUp() {
        taskService = new TaskService(taskRepository, appUserService);
        taskFilterService = new TaskFilterService(taskRepository,appUserService, taskService, expirationDaysWarning);
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
       AppUser customUser = createCustomUser();
       Task task = createdTask(TaskStatus.NEW,customUser);
       when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
       when(taskRepository.save(task)).thenReturn(createdTask(TaskStatus.IN_PROGRESS, customUser));
       TaskResponseDto result = taskService.inProgressTask(1L);
       assertEquals(TaskStatus.IN_PROGRESS,result.getTaskStatus());
    }

    @Test
    void should_throw_exception_when_task_is_already_in_progress() {
        AppUser customUser = createCustomUser();
        Task task = createdTask(TaskStatus.IN_PROGRESS,customUser);
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        assertThrows(BusinessException.class,() -> taskService.inProgressTask(1L));
    }


    Task createdTask(TaskStatus taskStatus, AppUser customUser) {
        return new Task(1L, TaskType.PREPARE_CLIENT_PRESENTATION,"test",
                taskStatus, customUser, customUser , LocalDateTime.now().minusDays(2),
                LocalDateTime.now().minusDays(2), LocalDateTime.now().plusDays(5));
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
        List<TaskResponseDto> expiredTasks = taskFilterService.getExpiredTasks();
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