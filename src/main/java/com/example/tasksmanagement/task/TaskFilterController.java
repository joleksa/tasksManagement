package com.example.tasksmanagement.task;

import com.example.tasksmanagement.dto.TaskFilterDto;
import com.example.tasksmanagement.dto.TaskResponseDto;
import com.example.tasksmanagement.task.taskEnum.TaskType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/task-filter")
@PreAuthorize("hasAnyAuthority({'admin:read', 'editor:read', 'user:read'})")
public class TaskFilterController {

    private final TaskService taskService;
    private final TaskFilterService taskFilterService;

    public TaskResponseDto responseDto(Task task) {
        return new TaskResponseDto(task.getTaskType(),
                task.getDescription(), task.getTaskStatus(), task.getExecutionDate());
    }
    @GetMapping
    ResponseEntity<TaskResponseDto> findTaskById(@RequestParam Long id) {
        TaskResponseDto taskById = responseDto(taskService.findTaskById(id));
        return ResponseEntity
                .ok()
                .body(taskById);
    }

    @GetMapping("/all")
    ResponseEntity<Page<TaskResponseDto>> getAllTasksSortedAndPaginated
            (@RequestParam int pageNo,
             @RequestParam int pageSize,
             @RequestParam String field,
             @RequestParam String direction) {
        Page<TaskResponseDto> allTasksSortedAndPaginated = taskFilterService.getAllTasksSortedAndPaginated(pageNo, pageSize, field, direction);
        return ResponseEntity
                .ok()
                .body(allTasksSortedAndPaginated);
    }

    @GetMapping("/warned-tasks")
    ResponseEntity<List<TaskResponseDto>> getWarnedTasks() {
        List<TaskResponseDto> warnedTasks = taskFilterService.getWarnedTasks();
        return ResponseEntity
                .ok()
                .body(warnedTasks);
    }

    @GetMapping("/expired-tasks")
    ResponseEntity<List<TaskResponseDto>> getExpiredTasks() {
        List<TaskResponseDto> expiredTasks = taskFilterService.getExpiredTasks();
        return ResponseEntity
                .ok()
                .body(expiredTasks);
    }

    @GetMapping("/created-by")
    ResponseEntity<List<TaskResponseDto>> getCreatedTasksByUser(@RequestParam Long id) {
        List<TaskResponseDto> createdTasksByUser = taskFilterService.getCreatedTasksByUser(id);
        return ResponseEntity.ok().body(createdTasksByUser);
    }

    @GetMapping("/assigned-to")
    ResponseEntity<List<TaskResponseDto>> getTasksAssignedToUser(@RequestParam Long id) {
        List<TaskResponseDto> assignedTasksToUser = taskFilterService.getAssignedTasksToUser(id);
        return ResponseEntity
                .ok()
                .body(assignedTasksToUser);
    }

    @GetMapping("/status")
    ResponseEntity<List<TaskResponseDto>> getTasksByStatus(@RequestParam String status) {
        List<TaskResponseDto> taskByStatus = taskFilterService.getTaskByStatus(status);
        return ResponseEntity
                .ok()
                .body(taskByStatus);
    }

    @GetMapping("/creation-date-range")
    ResponseEntity<List<TaskResponseDto>> getTasksByCreationDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)LocalDateTime endDate) {
        TaskFilterDto filterDto = new TaskFilterDto(startDate, endDate);
        List<TaskResponseDto> taskByCreationDateRange = taskFilterService.getTaskByCreationDateRange(filterDto);
        return ResponseEntity
                .ok()
                .body(taskByCreationDateRange);
    }

    @GetMapping("/execution-date-range")
    ResponseEntity<List<TaskResponseDto>> getTasksByExecutionDateRange(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)LocalDateTime endDate) {
        TaskFilterDto filterDto = new TaskFilterDto(startDate, endDate);
        List<TaskResponseDto> taskByExecutionDateRange = taskFilterService.getTaskByExecutionDateRange(filterDto);
        return ResponseEntity
                .ok()
                .body(taskByExecutionDateRange);
    }

    @GetMapping("/task-type")
    ResponseEntity<List<TaskResponseDto>> getTasksByType(@RequestParam TaskType taskType) {
        TaskFilterDto filterDto = new TaskFilterDto(taskType);
        List<TaskResponseDto> taskByType = taskFilterService.getTaskByType(filterDto);
        return ResponseEntity.ok().body(taskByType);
    }
}
