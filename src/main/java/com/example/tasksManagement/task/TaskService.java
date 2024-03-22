package com.example.tasksManagement.task;

import com.example.tasksManagement.BusinessException;
import com.example.tasksManagement.Dto.AssignTaskDto;
import com.example.tasksManagement.Dto.TaskDto;
import com.example.tasksManagement.Dto.TaskFilterDto;
import com.example.tasksManagement.Dto.TaskResponseDto;
import com.example.tasksManagement.task.taskEnum.TaskStatus;
import com.example.tasksManagement.user.AppUser;
import com.example.tasksManagement.user.AppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Service
public class TaskService {
    private final TaskRepository taskRepository;
    private final AppUserService appUserService;
    private final int expirationDaysWarning;

    @Autowired
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
                .build();
        task.setOptionalExecutionDate(daysToEnd);
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
        Task taskOptional = findTaskById(id);
        if (taskOptional.getTaskStatus() == TaskStatus.CLOSED) {
            throw new BusinessException("Task is already closed");
        }
        taskOptional.setTaskStatus(TaskStatus.CLOSED);
        return getResponseDto(saveModificatedTask(taskOptional));
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
        Task taskOptional = findTaskById(id);
        if (taskOptional.getTaskStatus() == TaskStatus.CANCELLED) {
            throw new BusinessException("Task is already cancelled");
        }
        taskOptional.setTaskStatus(TaskStatus.CANCELLED);
        return getResponseDto(saveModificatedTask(taskOptional));
    }

    public List<TaskResponseDto> getAllTasks() {
        return taskRepository.findAll()
                .stream().map(this::getResponseDto).toList();
    }


    public List<TaskResponseDto> getAllTasksSortedAndPaginated(int pageNo, int pageSize, String field, String direction) {
        Page<Task> tasks = getPageableTasks(pageNo, pageSize, field, direction);
        return tasks.getContent().stream()
                .map(this::getResponseDto).toList();
    }

    private Page<Task> getPageableTasks(int pageNo, int pageSize,
                                        String field, String direction) {
        Pageable pageable = PageRequest.of(pageNo, pageSize)
                .withSort(Sort.Direction.fromString(direction), field);
        return taskRepository.findAll(pageable);
    }

    public TaskResponseDto assignTask(AssignTaskDto assignTaskDto) {
        Task task = findTaskById(assignTaskDto.getTaskId());
        AppUser assignedUser = appUserService.findUserById(assignTaskDto.getUserId());
        assignTaskValidator(task, assignedUser);
        task.setAssignedUser(assignedUser);
        return getResponseDto(saveModificatedTask(task));
    }

    public List<TaskResponseDto> getWarnedTasks() {
        LocalDateTime warningDate = LocalDateTime.now()
                .plusDays(expirationDaysWarning);
        return taskRepository.findWarnedTasks(warningDate).stream()
                .map(this::getResponseDto)
                .toList();
    }


    private void assignTaskValidator(Task task, AppUser assignedUser) {
        if (task.getTaskStatus() != TaskStatus.NEW) {
            throw new BusinessException("Task must be in NEW status");
        }
        if (assignedUser == task.getAssignedUser()) {
            throw new BusinessException("Task is already assigned to this user");
        }
    }

    TaskResponseDto getResponseDto(Task task) {
        return new TaskResponseDto(task.getTaskType(),
                task.getDescription(), task.getTaskStatus(), task.getExecutionDate());
    }

    private Task saveModificatedTask(Task task) {
        task.setModificationDate(LocalDateTime.now());
        return taskRepository.save(task);
    }

    public List<TaskResponseDto> getExpiredTasks() {//searching by query
        LocalDateTime expiredDate = LocalDateTime.now();
        return taskRepository.findExpiredTasks(expiredDate).stream()
                .map(this::getResponseDto)
                .toList();
    }

    public List<TaskResponseDto> getAssignedTasksToUser(Long userId) {//searching by query
        AppUser assignedUser = appUserService.findUserById(userId);
        return assignedUser.getAssignedTasks().stream()
                .map(this::getResponseDto)
                .toList();
    }

    public List<TaskResponseDto> getCreatedTasksByUser(Long id) {//searching by query
        AppUser createdBy = appUserService.findUserById(id);
        return createdBy.getCreatedTasks().stream()
                .map(this::getResponseDto)
                .toList();
    }

    public List<TaskResponseDto> getTaskByStatus(String status) {//searching by query
        return taskRepository.findListByTaskStatus(TaskStatus.valueOf(status)).stream()
                .map(this::getResponseDto)
                .toList();
    }

    public List<TaskResponseDto> getTaskByType(TaskFilterDto filterDto) {//searching by filter
        return taskRepository.findAll().stream()
                .filter(task -> task.getTaskType() == filterDto.getTaskType())
                .map(this::getResponseDto)
                .toList();
    }

    public List<TaskResponseDto> getTaskByCreationDateRange(TaskFilterDto filterDto) {//searching by filter
        return taskRepository.findAll().stream()
                .filter(task -> task.getCreationDate().isAfter(filterDto.getStartDate()) &&
                        task.getCreationDate().isBefore(filterDto.getEndDate()))
                .map(this::getResponseDto)
                .toList();
    }

    public List<TaskResponseDto> getTaskByExecutionDateRange(TaskFilterDto filterDto) {
        return taskRepository.findAll().stream()
                .filter(task -> task.getExecutionDate() != null &&
                        task.getExecutionDate().isAfter(filterDto.getStartDate())
                        && task.getExecutionDate().isBefore(filterDto.getEndDate()))
                .map(this::getResponseDto)
                .toList();
    }

}
