package com.example.tasksmanagement.task;

import com.example.tasksmanagement.BusinessException;
import com.example.tasksmanagement.dto.AssignTaskDto;
import com.example.tasksmanagement.dto.TaskDto;
import com.example.tasksmanagement.dto.TaskResponseDto;
import com.example.tasksmanagement.task.taskEnum.TaskStatus;
import com.example.tasksmanagement.user.AppUser;
import com.example.tasksmanagement.user.AppUserService;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Optional;


@Service
public class TaskService {
    private final TaskRepository taskRepository;
    private final AppUserService appUserService;
    private final Clock clock;

    public TaskService(TaskRepository taskRepository,
                       AppUserService appUserService, Clock clock) {
        this.taskRepository = taskRepository;
        this.appUserService = appUserService;
        this.clock = clock;
    }

    public TaskResponseDto createTask(TaskDto taskDto) {
        AppUser assignedUser = appUserService.findUserById(taskDto.getAssignedUserId());
        AppUser createdByUser = appUserService.findUserById(taskDto.getCreatedById());
        Task task = createNewTask(taskDto, assignedUser, createdByUser);
        return getResponseDto(taskRepository.save(task));
    }

    private Task createNewTask(TaskDto taskDto, AppUser assignedUser, AppUser createdByUser) {
        Integer daysToEnd = taskDto.getDaysToEnd();
        Task task = Task.builder()
                .taskType(taskDto.getTaskType())
                .description(taskDto.getDescription())
                .taskStatus(TaskStatus.NEW)
                .assignedUser(assignedUser)
                .createdBy(createdByUser)
                .creationDate(LocalDateTime.now(clock))
                .modificationDate(LocalDateTime.now(clock))
                .build();
        task.setOptionalExecutionDate(daysToEnd,clock);
        return task;
    }

    public Task findTaskById(Long id) {
        Optional<Task> taskOptional = taskRepository.findById(id);
        if (taskOptional.isEmpty()) {
            throw new BusinessException("Task doesn't exist");
        }
        return taskOptional.get();
    }

    public TaskResponseDto closeTask(Long id) {
        Task task = findTaskById(id);
        if (task.getTaskStatus() == TaskStatus.CLOSED) {
            throw new BusinessException("Task is already closed");
        }
        task.setTaskStatus(TaskStatus.CLOSED);
        return getResponseDto(saveModificatedTask(task));
    }

    public TaskResponseDto inProgressTask(Long id) {
        Task task = findTaskById(id);
        if (task.getTaskStatus() == TaskStatus.IN_PROGRESS) {
            throw new BusinessException("Task is already in progress");
        }
        task.setTaskStatus(TaskStatus.IN_PROGRESS);
        return getResponseDto(saveModificatedTask(task));
    }

    public TaskResponseDto cancelTask(Long id) {
        Task task = findTaskById(id);
        if (task.getTaskStatus() == TaskStatus.CANCELLED) {
            throw new BusinessException("Task is already cancelled");
        }
        task.setTaskStatus(TaskStatus.CANCELLED);
        return getResponseDto(saveModificatedTask(task));
    }

    public TaskResponseDto assignTask(AssignTaskDto assignTaskDto) {
        Task task = findTaskById(assignTaskDto.getTaskId());
        AppUser assignedUser = appUserService.findUserById(assignTaskDto.getUserId());
        task.assignTo(task, assignedUser);
        return getResponseDto(saveModificatedTask(task));
    }

    TaskResponseDto getResponseDto(Task task) {
        return new TaskResponseDto(task.getTaskType(),
                task.getDescription(), task.getTaskStatus(), task.getExecutionDate());
    }

    private Task saveModificatedTask(Task task) {
        task.setModificationDate(LocalDateTime.now(clock));
        return taskRepository.save(task);
    }
}
