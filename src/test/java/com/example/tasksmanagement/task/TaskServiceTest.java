package com.example.tasksmanagement.task;

import com.example.tasksmanagement.BusinessException;
import com.example.tasksmanagement.dto.AssignTaskDto;
import com.example.tasksmanagement.dto.TaskResponseDto;
import com.example.tasksmanagement.task.taskEnum.TaskStatus;
import com.example.tasksmanagement.task.taskEnum.TaskType;
import com.example.tasksmanagement.user.AppUser;
import com.example.tasksmanagement.user.AppUserRepository;
import com.example.tasksmanagement.user.AppUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;
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

    public Clock clock() {
        return Clock.system(ZoneId.of("Europe/Warsaw"));
    }

    @BeforeEach
    void setUp() {
        taskService = new TaskService(taskRepository, appUserService, clock());
        taskFilterService = new TaskFilterService(taskRepository, appUserService, taskService, expirationDaysWarning, clock());
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
        Task task = createdTask(TaskStatus.NEW, customUser);
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(taskRepository.save(task)).thenReturn(createdTask(TaskStatus.CLOSED, customUser));
        TaskResponseDto result = taskService.closeTask(1L);
        assertEquals(TaskStatus.CLOSED, result.taskStatus());
    }

    @Test
    void should_throw_exception_when_task_is_already_closed() {
        AppUser customUser = createCustomUser();
        Task task = createdTask(TaskStatus.CLOSED, customUser);
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        assertThrows(BusinessException.class, () -> taskService.closeTask(1L));
    }

    @Test
    void should_change_task_status_to_in_progress() {
        AppUser customUser = createCustomUser();
        Task task = createdTask(TaskStatus.NEW, customUser);
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(taskRepository.save(task)).thenReturn(createdTask(TaskStatus.IN_PROGRESS, customUser));
        TaskResponseDto result = taskService.inProgressTask(1L);
        assertEquals(TaskStatus.IN_PROGRESS, result.taskStatus());
    }

    @Test
    void should_throw_exception_when_task_is_already_in_progress() {
        AppUser customUser = createCustomUser();
        Task task = createdTask(TaskStatus.IN_PROGRESS, customUser);
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        assertThrows(BusinessException.class, () -> taskService.inProgressTask(1L));
    }


    Task createdTask(TaskStatus taskStatus, AppUser customUser) {
        return new Task(1L, TaskType.PREPARE_CLIENT_PRESENTATION, "test",
                taskStatus, customUser, customUser, LocalDateTime.now(clock()).minusDays(2),
                LocalDateTime.now(clock()).minusDays(2), LocalDateTime.now(clock()).plusDays(5));
    }

    @Test
    void should_change_task_status_to_cancelled() {
        AppUser customUser = createCustomUser();
        Task task = createdTask(TaskStatus.NEW, customUser);
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(taskRepository.save(task)).thenReturn(createdTask(TaskStatus.CANCELLED, customUser));
        TaskResponseDto result = taskService.cancelTask(1L);
        assertEquals(TaskStatus.CANCELLED, result.taskStatus());
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
        List<Task> tasks = getTasksForMock(LocalDateTime.now(clock()).plusDays(10));
        Page<Task> taskPage = new PageImpl<>(tasks);
        when(taskRepository.findAll(any(Pageable.class))).thenReturn(taskPage);
        Page<TaskResponseDto> actualTasks = taskFilterService.getAllTasksSortedAndPaginated(0,10, "executionDate", "ASC");
        assertEquals(tasks.size(), actualTasks.getTotalElements());
    }

    @Test
    void should_throw_exception_when_task_is_already_assigned_to_user() {
        AppUser customUser = createCustomUser();
        Task task = createdTask(TaskStatus.NEW, customUser);
        AssignTaskDto assignTaskDto = new AssignTaskDto(task.getId(), customUser.getId());
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(appUserService.findUserById(1L)).thenReturn(customUser);
        BusinessException ex = assertThrows(BusinessException.class, () -> taskService.assignTask(assignTaskDto));
        assertTrue(ex.getMessage().contains("Task is already assigned to this user"));
    }

    @Test
    void should_throw_exception_when_task_is_not_in_new_status() {
        AppUser customUser = createCustomUser();
        Task task = createdTask(TaskStatus.IN_PROGRESS, customUser);
        AssignTaskDto assignTaskDto = new AssignTaskDto(task.getId(), customUser.getId());
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(appUserService.findUserById(1L)).thenReturn(customUser);
        BusinessException ex = assertThrows(BusinessException.class, () -> taskService.assignTask(assignTaskDto));
        assertTrue(ex.getMessage().contains("Task must be in NEW status"));
    }

    @Test
    void should_return_all_warned_tasks() {
        List<Task> tasks = getTasksForMock(LocalDateTime.now(clock()).plusDays(2));
        when(taskRepository.findWarnedTasks(any())).thenReturn(tasks);
        List<TaskResponseDto> warnedTasks = taskFilterService.getWarnedTasks();
        assertEquals(2, warnedTasks.size());
        assertTrue(tasks.stream()
                .allMatch(taskResponseDto -> taskResponseDto
                        .getExecutionDate()
                        .isBefore(LocalDateTime.now(clock())
                                .plusDays(expirationDaysWarning))));
    }

    @Test
    void should_return_all_expired_tasks() {
        List<Task> tasks = getTasksForMock(LocalDateTime.now(clock()).minusDays(1));
        when(taskRepository.findExpiredTasks(any())).thenReturn(tasks);
        List<TaskResponseDto> expiredTasks = taskFilterService.getExpiredTasks();
        assertEquals(2, expiredTasks.size());
        assertTrue(LocalDateTime.now(clock()).isAfter(expiredTasks.getFirst().executionDate()));
    }

    private List<Task> getTasksForMock(LocalDateTime expirationDate) {
        AppUser customUser = createCustomUser();
        Task task = new Task(1L, TaskType.CREATE_MONTHLY_REPORT, "test", TaskStatus.NEW, customUser, customUser, LocalDateTime.now(clock()).minusDays(2), LocalDateTime.now(clock()).minusDays(2), expirationDate);
        Task task2 = new Task(2L, TaskType.CREATE_MONTHLY_REPORT, "test2", TaskStatus.NEW, customUser, customUser, LocalDateTime.now(clock()).minusDays(2), LocalDateTime.now(clock()).minusDays(2), expirationDate);
        return List.of(task, task2);
    }

    private AppUser createCustomUser() {
        return new AppUser(1L, "Adam", "Klocek", "akloc");
    }
}