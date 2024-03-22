package com.example.tasksManagement.user;

import com.example.tasksManagement.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("api/user")
public class AppUserController {

    private final AppUserService appUserService;

    @Autowired
    public AppUserController(AppUserService appUserService) {
        this.appUserService = appUserService;
    }

    @GetMapping("/all")
    ResponseEntity<List<AppUser>> findAllUsers() {
        return ResponseEntity
                .ok()
                .body(appUserService.getAppUsers());
    }

    @GetMapping
    ResponseEntity<AppUser> findUser(@RequestParam Long id) {
        return ResponseEntity
                .ok()
                .body(appUserService.findUserById(id));
    }

    @PostMapping
    ResponseEntity<Void> registerNewUser(@RequestBody AppUser appUser) {
        appUserService.addNewUser(appUser);
        return ResponseEntity
                .ok()
                .build();
    }

    @DeleteMapping
    ResponseEntity<Void> deleteUser(@RequestParam String login) {
        appUserService.deleteUser(login);
        return ResponseEntity
                .ok()
                .build();
    }

}
