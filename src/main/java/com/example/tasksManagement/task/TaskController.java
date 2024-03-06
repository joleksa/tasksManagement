package com.example.tasksManagement.task;

import com.example.tasksManagement.Dto.TaskDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor //tworzy konstruktor z wymaganymi polami, czyli taskService
@RequestMapping("api/task")
public class TaskController {
    private final TaskService taskService;

    @PostMapping
    public Task createNewTask(@RequestBody TaskDto taskDto) {
        return taskService.createTask(taskDto);
    }

    @GetMapping
    public Task findTaskById(@RequestParam Long id) { return taskService.findTaskById(id);}

    @GetMapping("/all")
    public List<Task> getAllTasks() { return taskService.getAllTasks();}

    @PatchMapping("/close")
    public Task closeTask(@RequestParam Long id) { return taskService.closeTask(id);}

    @PatchMapping("/in-progress")
    public Task inProgressTask(@RequestParam Long id) { return taskService.inProgressTask(id);}

    @PatchMapping("/cancel")
    public Task cancelTask(@RequestParam Long id) { return taskService.cancelTask(id);}
}
