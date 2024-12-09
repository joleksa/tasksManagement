package com.example.tasksmanagement.user;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/common")
public class CommonController {

    private final CommonService service;

    @DeleteMapping("/delete-user-and-tasks")
    ResponseEntity<Void> deleteUserAndTasks(@RequestParam String login) {
        service.deleteTasksAndUser(login);
        return ResponseEntity
                .ok()
                .build();
    }
}
