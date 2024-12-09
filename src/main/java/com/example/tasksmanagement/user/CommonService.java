package com.example.tasksmanagement.user;

import com.example.tasksmanagement.task.Task;
import com.example.tasksmanagement.task.TaskFilterService;
import com.example.tasksmanagement.task.TaskService;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CommonService {
    private final TaskFilterService taskFilterService;
    private final TaskService taskService;
    private final AppUserService appUserService;

    @Transactional
    public void deleteTasksAndUser(String login) {
        List<Task> assignedTasksToUser = taskFilterService.findTasksByAssignedUser(login);
        taskService.deleteAllTasks(assignedTasksToUser);
        appUserService.deleteUser(login);
    }
}
