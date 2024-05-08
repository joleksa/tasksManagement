package com.example.tasksmanagement.task;

import com.example.tasksmanagement.dto.TaskFilterDto;
import com.example.tasksmanagement.dto.TaskResponseDto;
import com.example.tasksmanagement.task.taskEnum.TaskStatus;
import com.example.tasksmanagement.user.AppUser;
import com.example.tasksmanagement.user.AppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;


@Service
public class TaskFilterService {

    private final TaskRepository taskRepository;
    private final AppUserService appUserService;
    private final TaskService taskService;
    private final int expirationDaysWarning;

    @Autowired
    public TaskFilterService(TaskRepository taskRepository,
                       AppUserService appUserService,
                       TaskService taskService,
                       @Value("${task.expiration-warning-days}")
                       int expirationDaysWarning) {
        this.taskRepository = taskRepository;
        this.appUserService = appUserService;
        this.taskService = taskService;
        this.expirationDaysWarning = expirationDaysWarning;
    }


    public List<TaskResponseDto> getAllTasks() {
        return taskRepository.findAll()
                .stream().map(taskService::getResponseDto).toList();
    }
    public Page<TaskResponseDto> getAllTasksSortedAndPaginated(int pageNo, int pageSize, String field, String direction) {
        Page<Task> tasks = getPageableTasks(pageNo, pageSize, field, direction);
        List<TaskResponseDto> listTaskResponseDto =  tasks.getContent().stream()
                .map(taskService::getResponseDto).toList();

        return new PageImpl<>(listTaskResponseDto, tasks.getPageable(), tasks.getTotalElements());
    }

    private Page<Task> getPageableTasks(int pageNo, int pageSize,
                                        String field, String direction) {
        Pageable pageable = PageRequest.of(pageNo, pageSize)
                .withSort(Sort.Direction.fromString(direction), field);
        return taskRepository.findAll(pageable);
    }

    public List<TaskResponseDto> getWarnedTasks() {
        LocalDateTime warningDate = LocalDateTime.now()
                .plusDays(expirationDaysWarning);
        return taskRepository.findWarnedTasks(warningDate).stream()
                .map(taskService::getResponseDto)
                .toList();
    }

    public List<TaskResponseDto> getExpiredTasks() {//searching by query
        LocalDateTime expiredDate = LocalDateTime.now();
        return taskRepository.findExpiredTasks(expiredDate).stream()
                .map(taskService::getResponseDto)
                .toList();
    }

    public List<TaskResponseDto> getAssignedTasksToUser(Long userId) {//searching by query
        AppUser assignedUser = appUserService.findUserById(userId);
        return assignedUser.getAssignedTasks().stream()
                .map(taskService::getResponseDto)
                .toList();
    }

    public List<TaskResponseDto> getCreatedTasksByUser(Long id) {//searching by query
        AppUser createdBy = appUserService.findUserById(id);
        return createdBy.getCreatedTasks().stream()
                .map(taskService::getResponseDto)
                .toList();
    }

    public List<TaskResponseDto> getTaskByStatus(String status) {//searching by query
        return taskRepository.findListByTaskStatus(TaskStatus.valueOf(status)).stream()
                .map(taskService::getResponseDto)
                .toList();
    }

    public List<TaskResponseDto> getTaskByType(TaskFilterDto filterDto) {//searching by filter
        return taskRepository.findAll().stream()
                .filter(task -> task.getTaskType() == filterDto.getTaskType())
                .map(taskService::getResponseDto)
                .toList();
    }

    public List<TaskResponseDto> getTaskByCreationDateRange(TaskFilterDto filterDto) {//searching by filter
        return taskRepository.findAll().stream()
                .filter(task -> task.getCreationDate().isAfter(filterDto.getStartDate()) &&
                        task.getCreationDate().isBefore(filterDto.getEndDate()))
                .map(taskService::getResponseDto)
                .toList();
    }

    public List<TaskResponseDto> getTaskByExecutionDateRange(TaskFilterDto filterDto) {
        return taskRepository.findAll().stream()
                .filter(task -> task.getExecutionDate() != null &&
                        task.getExecutionDate().isAfter(filterDto.getStartDate())
                        && task.getExecutionDate().isBefore(filterDto.getEndDate()))
                .map(taskService::getResponseDto)
                .toList();
    }
}
