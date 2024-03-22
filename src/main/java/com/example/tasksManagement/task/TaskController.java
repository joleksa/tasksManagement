package com.example.tasksManagement.task;

import com.example.tasksManagement.Dto.AssignTaskDto;
import com.example.tasksManagement.Dto.TaskDto;
import com.example.tasksManagement.Dto.TaskFilterDto;
import com.example.tasksManagement.Dto.TaskResponseDto;
import com.example.tasksManagement.task.taskEnum.TaskType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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
    private final TaskFilterService taskFilterService;

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
    ResponseEntity<Task> findTaskById(@RequestParam Long id) { return new ResponseEntity<>(taskService.findTaskById(id),HttpStatus.OK);}

    @GetMapping("/all")
    ResponseEntity<List<TaskResponseDto>> getAllTasks() {
        return new ResponseEntity<>(taskFilterService.getAllTasks(), HttpStatus.OK);}

    @GetMapping("/all-sorted")
    ResponseEntity<Page<TaskResponseDto>> getAllTasksSortedAndPaginated
            (@RequestParam int pageNo,
             @RequestParam int pageSize,
             @RequestParam String field,
             @RequestParam String direction) {
        return new ResponseEntity<>(taskFilterService
                .getAllTasksSortedAndPaginated(pageNo, pageSize, field, direction)
                , HttpStatus.OK);}

    @GetMapping("/warned-tasks")
    ResponseEntity<List<TaskResponseDto>> getWarnedTasks() {
        return new ResponseEntity<>(taskFilterService.getWarnedTasks(),HttpStatus.OK);}

    @GetMapping("/expired-tasks")
    ResponseEntity<List<TaskResponseDto>> getExpiredTasks() {
        return new ResponseEntity<>(taskFilterService.getExpiredTasks(),HttpStatus.OK);
    }

    @GetMapping("/created-by")
    ResponseEntity<List<TaskResponseDto>> getCreatedTasksByUser(@RequestParam Long id) {
        return new ResponseEntity<>(taskFilterService.getCreatedTasksByUser(id), HttpStatus.OK);
    }

    @GetMapping("/assigned-to")
    ResponseEntity<List<TaskResponseDto>> getTasksAssignedToUser(@RequestParam Long id) {
        return new ResponseEntity<>(taskFilterService.getAssignedTasksToUser(id), HttpStatus.OK);
    }

    @GetMapping("/status")
    ResponseEntity<List<TaskResponseDto>> getTasksByStatus(@RequestParam String status) {
        return new ResponseEntity<>(taskFilterService.getTaskByStatus(status), HttpStatus.OK);
    }

    @GetMapping("/creation-date-range")
    ResponseEntity<List<TaskResponseDto>> getTasksByCreationDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)LocalDateTime endDate) {
        TaskFilterDto filterDto = new TaskFilterDto(startDate, endDate);
        return new ResponseEntity<>(taskFilterService.getTaskByCreationDateRange(filterDto),HttpStatus.OK);
    }

    @GetMapping("/execution-date-range")
    ResponseEntity<List<TaskResponseDto>> getTasksByExecutionDateRange(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)LocalDateTime endDate) {
        TaskFilterDto filterDto = new TaskFilterDto(startDate, endDate);
        return new ResponseEntity<>(taskFilterService.getTaskByExecutionDateRange(filterDto),HttpStatus.OK);
    }

    @GetMapping("/task-type")
    ResponseEntity<List<TaskResponseDto>> getTasksByType(@RequestParam TaskType taskType) {
        TaskFilterDto filterDto = new TaskFilterDto(taskType);
        return new ResponseEntity<>(taskFilterService.getTaskByType(filterDto),HttpStatus.OK);
    }



}
