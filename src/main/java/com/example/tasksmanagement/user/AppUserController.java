package com.example.tasksmanagement.user;

import com.example.tasksmanagement.dto.AppUserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("api/users")
public class AppUserController {

    private final AppUserService appUserService;

    @Autowired
    public AppUserController(AppUserService appUserService) {
        this.appUserService = appUserService;
    }

    @PreAuthorize("hasAuthority({'admin:read'})")
    @GetMapping("/all")
    public ResponseEntity<List<AppUserDto>> findAllUsers() {
        return ResponseEntity
                .ok()
                .body(appUserService.getAppUsers().stream().map(this::toDto).toList());
    }

    @GetMapping("/user")
    @PreAuthorize("hasAuthority({'admin:read'})")
    public ResponseEntity<AppUserDto> findUser(@RequestParam Long id) {
        return ResponseEntity
                .ok()
                .body(toDto(appUserService.findUserById(id)));
    }

    @PostMapping
    @PreAuthorize("hasAuthority({'admin:create'})")
    public ResponseEntity<Void> registerNewUser(@RequestBody AppUser appUser) {
        appUserService.addNewUser(appUser);
        return ResponseEntity
                .ok()
                .build();
    }

    @DeleteMapping
    @PreAuthorize("hasAuthority({'admin:delete'})")
    public ResponseEntity<Void> deleteUser(@RequestParam String login) {
        appUserService.deleteUser(login);
        return ResponseEntity
                .ok()
                .build();
    }

    private AppUserDto toDto(AppUser appUser) {
        return new AppUserDto(appUser.getId(),
                appUser.getName(),
                appUser.getSurname(),
                appUser.getLogin());
    }

}
