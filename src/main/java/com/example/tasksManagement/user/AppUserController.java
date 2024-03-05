package com.example.tasksManagement.user;

import org.springframework.beans.factory.annotation.Autowired;
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
    public AppUser findUser(@RequestParam String login) {
        return appUserService.findUser(login);
    }

    @PostMapping
    public void registerNewUser(@RequestBody AppUser appUser) {
        appUserService.addNewUser(appUser);
    }

    @DeleteMapping
    public void deleteUser(@RequestParam String login) {
        appUserService.deleteUser(login);
    }

}
