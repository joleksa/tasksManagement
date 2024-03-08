package com.example.tasksManagement.task;

import com.example.tasksManagement.BusinessException;
import com.example.tasksManagement.Dto.AssignTaskDto;
import com.example.tasksManagement.Dto.TaskDto;
import com.example.tasksManagement.Dto.TaskResponseDto;
import com.example.tasksManagement.task.taskEnum.TaskStatus;
import com.example.tasksManagement.user.AppUser;
import com.example.tasksManagement.user.AppUserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class TaskService {
    private final TaskRepository taskRepository;
    private final AppUserService appUserService;
    private final int expirationDaysWarning;

    public TaskService(TaskRepository taskRepository,
                       AppUserService appUserService,
                       @Value("${task.expiration-warning-days}")
                       int expirationDaysWarning) {
        this.taskRepository = taskRepository;
        this.appUserService = appUserService;
        this.expirationDaysWarning = expirationDaysWarning;
    }


    public TaskResponseDto createTask(TaskDto taskDto) {
        AppUser assignedUser = appUserService.findUserById(taskDto.getAssignedUserId());
        AppUser createdByUser = appUserService.findUserById(taskDto.getCreatedById());
        Task task = toModel(taskDto, assignedUser, createdByUser);
        return getResponseDto(taskRepository.save(task));
    }



    private Task toModel(TaskDto taskDto, AppUser assignedUser, AppUser createdByUser) {
        int daysToEnd = prepareExecutionDate(taskDto.getDaysToEnd());
        return daysToEnd == 0 //if
                ? new Task(taskDto.getTaskType(),
                taskDto.getDescription(),
                assignedUser,
                createdByUser)
                : new Task(taskDto.getTaskType(),
                taskDto.getDescription(),
                assignedUser,
                createdByUser,
                daysToEnd);
    }

    private int prepareExecutionDate(int daysToEnd) {
        if (daysToEnd < 0) {
            throw new BusinessException("Days cannot be negative");
        }
        return daysToEnd;
    }

    public Task findTaskById(Long id) {
        Optional<Task> taskOptional = taskRepository.findById(id);
        if (taskOptional.isEmpty()) {
            throw new BusinessException("Task doesn't exist");
        }
        return taskOptional.get();
    }

    public TaskResponseDto closeTask(Long id) {
        Task taskOptional = findTaskById(id);
        if (taskOptional.getTaskStatus() == TaskStatus.CLOSED) {
            throw new BusinessException("Task is already closed");
        }
        taskOptional.setTaskStatus(TaskStatus.CLOSED);
        return getResponseDto(saveModificatedTask(taskOptional));
    }

    public TaskResponseDto inProgressTask(Long id) {
        Task taskOptional = findTaskById(id);
        if (taskOptional.getTaskStatus() == TaskStatus.IN_PROGRESS) {
            throw new BusinessException("Task is already in progress");
        }
        taskOptional.setTaskStatus(TaskStatus.IN_PROGRESS);
        return getResponseDto(saveModificatedTask(taskOptional));
    }

    public TaskResponseDto cancelTask(Long id) {
        Task taskOptional = findTaskById(id);
        if (taskOptional.getTaskStatus() == TaskStatus.CANCELLED) {
            throw new BusinessException("Task is already cancelled");
        }
        taskOptional.setTaskStatus(TaskStatus.CANCELLED);
        return getResponseDto(saveModificatedTask(taskOptional));
    }

    public List<TaskResponseDto> getAllTasks(int pageNo, int pageSize) {
        Page<Task> tasks = getPageableTasks(pageNo, pageSize);
        return tasks.getContent().stream()
                .map(this::getResponseDto)
                .collect(Collectors.toList());
    }

    private Page<Task> getPageableTasks(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        return taskRepository.findAll(pageable);
    }

    public TaskResponseDto assignTask(AssignTaskDto assignTaskDto) {
        Task task = findTaskById(assignTaskDto.getTaskId());
        AppUser assignedUser = appUserService.findUserById(assignTaskDto.getUserId());
        assignTaskValidator(task, assignedUser);
        task.setAssignedUser(assignedUser);
        return getResponseDto(saveModificatedTask(task));
    }

    public List <TaskResponseDto> getWarnedTasks() {
        LocalDateTime warningDate = LocalDateTime.now()
                .plusDays(expirationDaysWarning);
        return taskRepository.findWarnedTasks(warningDate)
                .stream().map(this::getResponseDto).toList();
    }


    private void assignTaskValidator(Task task, AppUser assignedUser) {
        if (task.getTaskStatus() != TaskStatus.NEW) {
            throw new BusinessException("Task must be in NEW status");
        }
        if (assignedUser == task.getAssignedUser()) {
            throw new BusinessException("Task is already assigned to this user");
        }
    }

    private TaskResponseDto getResponseDto(Task task) {
        TaskResponseDto taskResponseDto = new TaskResponseDto();
        taskResponseDto.setTaskStatus(task.getTaskStatus());
        taskResponseDto.setTaskType(task.getTaskType());
        taskResponseDto.setDescription(task.getDescription());
        taskResponseDto.setExecutionDate(task.getExecutionDate());
        return taskResponseDto;
    }

    private Task saveModificatedTask(Task task) {
        task.setModificationDate(LocalDateTime.now());
        return taskRepository.save(task);
    }

    public List<TaskResponseDto> getExpiredTasks() {
        LocalDateTime expiredDate = LocalDateTime.now();
        return taskRepository.findExpiredTasks(expiredDate)
               .stream().map(this::getResponseDto).toList();
    }
}
