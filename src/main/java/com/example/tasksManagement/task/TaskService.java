package com.example.tasksManagement.task;

import com.example.tasksManagement.BusinessException;
import com.example.tasksManagement.Dto.AssignTaskDto;
import com.example.tasksManagement.Dto.TaskDto;
import com.example.tasksManagement.Dto.TaskResponseDto;
import com.example.tasksManagement.task.taskEnum.TaskStatus;
import com.example.tasksManagement.user.AppUser;
import com.example.tasksManagement.user.AppUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class TaskService {
    private final TaskRepository taskRepository;
    private final AppUserService appUserService;


    public TaskResponseDto createTask(TaskDto taskDto) {
        AppUser assignedUser = appUserService.findUserById(taskDto.getAssignedUserId());
        AppUser createdByUser = appUserService.findUserById(taskDto.getCreatedById());
        Task task = toModel(taskDto, assignedUser, createdByUser);
        Task createdTask = taskRepository.save(task);

        return getTaskResponseDto(createdTask);
    }

    private TaskResponseDto getTaskResponseDto(Task createdTask) {
        TaskResponseDto taskResponseDto = new TaskResponseDto();
        taskResponseDto.setTaskType(createdTask.getTaskType());
        taskResponseDto.setDescription(createdTask.getDescription());
        taskResponseDto.setTaskStatus(createdTask.getTaskStatus());
        return taskResponseDto;
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
        Task modificatedTask = saveModificatedTask(taskOptional);
        return getResponseDto(modificatedTask);
    }

    public TaskResponseDto inProgressTask(Long id) {
        Task taskOptional = findTaskById(id);
        if (taskOptional.getTaskStatus() == TaskStatus.IN_PROGRESS) {
            throw new BusinessException("Task is already in progress");
        }
        taskOptional.setTaskStatus(TaskStatus.IN_PROGRESS);
        Task modificatedTask = saveModificatedTask(taskOptional);
        return getResponseDto(modificatedTask);
    }

    public TaskResponseDto cancelTask(Long id) {
        Task taskOptional = findTaskById(id);
        if (taskOptional.getTaskStatus() == TaskStatus.CANCELLED) {
            throw new BusinessException("Task is already cancelled");
        }
        taskOptional.setTaskStatus(TaskStatus.CANCELLED);
        Task modificatedTask = saveModificatedTask(taskOptional);
        return getResponseDto(modificatedTask);
    }

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public TaskResponseDto assignTask(AssignTaskDto assignTaskDto) {
        Task task = findTaskById(assignTaskDto.getTaskId());
        AppUser assignedUser = appUserService.findUserById(assignTaskDto.getUserId());
        assignTaskValidator(task, assignedUser);
        task.setAssignedUser(assignedUser);
        Task modificatedTask = saveModificatedTask(task);
        return getResponseDto(modificatedTask);
    }

    private void assignTaskValidator(Task task, AppUser assignedUser) {
        if (task.getTaskStatus() != TaskStatus.NEW) {
            throw new BusinessException("Task must be in NEW status");
        }
        if (assignedUser == task.getAssignedUser()) {
            throw new BusinessException("Task is already assigned to this user");
        }
    }

    private TaskResponseDto getResponseDto(Task modificatedTask) {
        TaskResponseDto taskResponseDto = new TaskResponseDto();
        taskResponseDto.setTaskStatus(modificatedTask.getTaskStatus());
        taskResponseDto.setTaskType(modificatedTask.getTaskType());
        taskResponseDto.setDescription(modificatedTask.getDescription());
        return taskResponseDto;
    }

    private Task saveModificatedTask(Task task) {
        task.setModificationDate(LocalDateTime.now());
        return taskRepository.save(task);
    }
}
