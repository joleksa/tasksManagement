package com.example.tasksManagement.task;

import com.example.tasksManagement.BusinessException;
import com.example.tasksManagement.Dto.AssignTaskDto;
import com.example.tasksManagement.Dto.TaskResponseDto;
import com.example.tasksManagement.task.taskEnum.TaskStatus;
import com.example.tasksManagement.task.taskEnum.TaskType;
import com.example.tasksManagement.user.AppUser;
import com.example.tasksManagement.user.AppUserRepository;
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
    private final AppUserRepository appUserRepository = Mockito.mock(AppUserRepository.class);
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
    void should_change_task_status_to_closed() {
        AppUser customUser = createCustomUser();
        Task task = createdTask(TaskStatus.NEW,customUser);
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(taskRepository.save(task)).thenReturn(createdTask(TaskStatus.CLOSED, customUser));
        TaskResponseDto result = taskService.closeTask(1L);
        assertEquals(TaskStatus.CLOSED,result.getTaskStatus());
    }

    @Test
    void should_throw_exception_when_task_is_already_closed() {
        AppUser customUser = createCustomUser();
        Task task = createdTask(TaskStatus.CLOSED,customUser);
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        assertThrows(BusinessException.class,() -> taskService.closeTask(1L));
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
    void should_change_task_status_to_cancelled() {
        AppUser customUser = createCustomUser();
        Task task = createdTask(TaskStatus.NEW,customUser);
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(taskRepository.save(task)).thenReturn(createdTask(TaskStatus.CANCELLED, customUser));
        TaskResponseDto result = taskService.cancelTask(1L);
        assertEquals(TaskStatus.CANCELLED, result.getTaskStatus());
    }

    @Test
    void should_throw_exception_when_task_is_already_cancelled() {
        AppUser customUser = createCustomUser();
        Task task = createdTask(TaskStatus.CANCELLED, customUser);
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        assertThrows(BusinessException.class, () -> taskService.cancelTask(1L));
    }

    @Test
    void getAllTasks() {
        List<Task> tasks = getTasksForMock(LocalDateTime.now().plusDays(10));
        when(taskRepository.findAll()).thenReturn(tasks);
        List<TaskResponseDto> actualTasks = taskFilterService.getAllTasks();
        assertEquals(tasks.size(),actualTasks.size());
    }

    @Test
    void should_throw_exception_when_task_is_already_assigned_to_user() {
        AppUser customUser = createCustomUser();
        Task task = createdTask(TaskStatus.NEW, customUser);
        AssignTaskDto assignTaskDto = new AssignTaskDto();
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        assignTaskDto.setTaskId(task.getId());
        when(appUserService.findUserById(1L)).thenReturn(customUser);
        assignTaskDto.setUserId(customUser.getId());
        BusinessException ex = assertThrows(BusinessException.class, () -> taskService.assignTask(assignTaskDto));
        assertTrue(ex.getMessage().contains("Task is already assigned to this user"));
    }

    @Test
    void should_throw_exception_when_task_is_not_in_new_status() {
        AppUser customUser = createCustomUser();
        Task task = createdTask(TaskStatus.IN_PROGRESS, customUser);
        AssignTaskDto assignTaskDto = new AssignTaskDto();
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        assignTaskDto.setTaskId(task.getId());
        when(appUserService.findUserById(1L)).thenReturn(customUser);
        assignTaskDto.setUserId(customUser.getId());
        BusinessException ex = assertThrows(BusinessException.class, () -> taskService.assignTask(assignTaskDto));
        assertTrue(ex.getMessage().contains("Task must be in NEW status"));
    }

    @Test
    void should_return_all_warned_tasks() {
        List<Task> tasks = getTasksForMock(LocalDateTime.now().plusDays(2));
        when(taskRepository.findWarnedTasks(any())).thenReturn(tasks);
        List<TaskResponseDto> warnedTasks = taskFilterService.getWarnedTasks();
        assertEquals(2,warnedTasks.size());
        assertTrue(tasks.stream()
                .allMatch(taskResponseDto -> taskResponseDto
                        .getExecutionDate()
                        .isBefore(LocalDateTime.now()
                        .plusDays(expirationDaysWarning))));
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
        return new AppUser( 1L,"Adam", "Klocek", "akloc");
    }
}