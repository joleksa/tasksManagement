package com.example.tasksManagement.task;

import com.example.tasksManagement.Dto.AssignTaskDto;
import com.example.tasksManagement.Dto.TaskDto;
import com.example.tasksManagement.Dto.TaskFilterDto;
import com.example.tasksManagement.Dto.TaskResponseDto;
import com.example.tasksManagement.task.taskEnum.TaskType;
import lombok.RequiredArgsConstructor;
import org.springframework.cglib.core.Local;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor //tworzy konstruktor z wymaganymi polami, czyli taskService
@RequestMapping("api/task")
public class TaskController {
    private final TaskService taskService;

    @PostMapping
    ResponseEntity<TaskResponseDto> createNewTask(@RequestBody TaskDto taskDto) {
        return new ResponseEntity<>(taskService.createTask(taskDto),HttpStatus.CREATED);
    }

    @PatchMapping("/close")
    ResponseEntity<TaskResponseDto> closeTask(@RequestParam Long id) {
        return new ResponseEntity<>(taskService.closeTask(id), HttpStatus.OK);}

    @PatchMapping("/in-progress")
    ResponseEntity<TaskResponseDto> inProgressTask(@RequestParam Long id) {
        return new ResponseEntity<>(taskService.inProgressTask(id),HttpStatus.OK);}

    @PatchMapping("/cancel")
    ResponseEntity<TaskResponseDto> cancelTask(@RequestParam Long id) {
        return new ResponseEntity<>(taskService.cancelTask(id),HttpStatus.OK);}
    @PatchMapping("/assign-task")
    ResponseEntity<TaskResponseDto> assignTask(@RequestBody AssignTaskDto assignTaskDto) {
        return new ResponseEntity<>(taskService.assignTask(assignTaskDto),HttpStatus.OK);}

    @GetMapping
    public Task findTaskById(@RequestParam Long id) { return taskService.findTaskById(id);}

    @GetMapping("/all")
    ResponseEntity<List<TaskResponseDto>> getAllTasks() {
        return new ResponseEntity<>(taskService.getAllTasks(), HttpStatus.OK);}

    @GetMapping("/all-sorted")
    ResponseEntity<List<TaskResponseDto>> getAllTasksSortedAndPaginated
            (@RequestParam int pageNo,
             @RequestParam int pageSize,
             @RequestParam String field,
             @RequestParam String direction) {
        return new ResponseEntity<>(taskService
                .getAllTasksSortedAndPaginated(pageNo, pageSize, field, direction)
                , HttpStatus.OK);}

    @GetMapping("/warned-tasks")
    ResponseEntity<List<TaskResponseDto>> getWarnedTasks() {
        return new ResponseEntity<>(taskService.getWarnedTasks(),HttpStatus.OK);}

    @GetMapping("/expired-tasks")
    ResponseEntity<List<TaskResponseDto>> getExpiredTasks() {
        return new ResponseEntity<>(taskService.getExpiredTasks(),HttpStatus.OK);
    }

    @GetMapping("/created-by")
    ResponseEntity<List<TaskResponseDto>> getCreatedTasksByUser(@RequestParam Long id) {
        return new ResponseEntity<>(taskService.getCreatedTasksByUser(id), HttpStatus.OK);
    }

    @GetMapping("/assigned-to")
    ResponseEntity<List<TaskResponseDto>> getTasksAssignedToUser(@RequestParam Long id) {
        return new ResponseEntity<>(taskService.getAssignedTasksToUser(id), HttpStatus.OK);
    }

    @GetMapping("/status")
    ResponseEntity<List<TaskResponseDto>> getTasksByStatus(@RequestParam String status) {
        return new ResponseEntity<>(taskService.getTaskByStatus(status), HttpStatus.OK);
    }

    @GetMapping("/creation-date-range")
    ResponseEntity<List<TaskResponseDto>> getTasksByCreationDateRange(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)LocalDateTime endDate) {
        TaskFilterDto filterDto = new TaskFilterDto(startDate, endDate);
        return new ResponseEntity<>(taskService.getTaskByCreationDateRange(filterDto),HttpStatus.OK);
    }

    @GetMapping("/execution-date-range")
    ResponseEntity<List<TaskResponseDto>> getTasksByExecutionDateRange(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)LocalDateTime endDate) {
        TaskFilterDto filterDto = new TaskFilterDto(startDate, endDate);
        return new ResponseEntity<>(taskService.getTaskByExecutionDateRange(filterDto),HttpStatus.OK);
    }

    @GetMapping("/task-type")
    ResponseEntity<List<TaskResponseDto>> getTasksByType(@RequestParam TaskType taskType) {
        TaskFilterDto filterDto = new TaskFilterDto(taskType);
        return new ResponseEntity<>(taskService.getTaskByType(filterDto),HttpStatus.OK);
    }



}
