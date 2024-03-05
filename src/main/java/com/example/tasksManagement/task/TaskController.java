package com.example.tasksManagement.task;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor //tworzy konstruktor z wymaganymi polami, czyli taskService
@RequestMapping("api/task")
public class TaskController {
    private final TaskService taskService;

    @GetMapping
    public String helloWorld() {
        return taskService.helloWorld();
    }
}
