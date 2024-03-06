package com.example.tasksManagement.task;

import com.example.tasksManagement.BusinessException;
import com.example.tasksManagement.Dto.TaskDto;
import com.example.tasksManagement.task.taskEnum.TaskStatus;
import com.example.tasksManagement.task.taskEnum.TaskType;
import com.example.tasksManagement.user.AppUser;
import com.example.tasksManagement.user.AppUserRepository;
import com.example.tasksManagement.user.AppUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class TaskService {
    private final TaskRepository taskRepository;
    private final AppUserService appUserService;


    public Task createTask(TaskDto taskDto) {
        AppUser assignedUser = appUserService.findUserById(taskDto.getAssignedUserId());
        AppUser createdByUser = appUserService.findUserById(taskDto.getCreatedById());
        Task task = toModel(taskDto, assignedUser, createdByUser);
        return taskRepository.save(task);
    }

    private Task toModel(TaskDto taskDto, AppUser assignedUser, AppUser createdByUser) {
        return new Task(taskDto.getTaskType(),
                taskDto.getDescription(),
                assignedUser,
                createdByUser,
                taskDto.getDaysToEnd());
    }

    public Task findTaskById(Long id) {
        Optional<Task> taskOptional = taskRepository.findById(id);
        if (taskOptional.isEmpty()) {
            throw new BusinessException("task doesn't exist");
        }
        return taskOptional.get();
    }

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }
}
