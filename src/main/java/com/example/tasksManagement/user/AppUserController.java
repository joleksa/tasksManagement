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
    public List<AppUser> findAllUsers() {
        return appUserService.getAppUsers();
    }

    @GetMapping
    public AppUser findUser(@RequestParam Long id) {
        return appUserService.findUserById(id);
    }

    @PostMapping
    public void registerNewUser(@RequestBody AppUser appUser) {
        appUserService.addNewUser(appUser);
    }

    @DeleteMapping
    public void deleteUser(@RequestParam String login) {
        appUserService.deleteUser(login);
    }

    @GetMapping("/createdBy")
    ResponseEntity<List<Task>> getCreatedTasksByUser(@RequestParam Long id) { return new ResponseEntity<>(appUserService.getTasksCreatedBy(id), HttpStatus.OK);}
}
