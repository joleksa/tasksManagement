package com.example.tasksmanagement.user;

import com.example.tasksmanagement.dto.AppUserDto;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/users")
public class AppUserController {

    private final AppUserService appUserService;

    public AppUserController(AppUserService appUserService) {
        this.appUserService = appUserService;
    }

    @PreAuthorize("hasAuthority({'admin:read'})")
    @GetMapping("/all")
    public ResponseEntity<Page<AppUserDto>> getAllUsersSortedAndPaginated
            (@RequestParam int pageNo,
             @RequestParam int pageSize,
             @RequestParam String field,
             @RequestParam String direction) {
        Page<AppUserDto> allUsersSortedAndPaginated = appUserService.getAllUsersSortedAndPaginated(pageNo, pageSize, field, direction);
        return ResponseEntity
                .ok()
                .body(allUsersSortedAndPaginated);
    }

    @GetMapping("/user")
    @PreAuthorize("hasAuthority({'admin:read'})")
    public ResponseEntity<AppUserDto> findUser(@RequestParam Long id) {
        return ResponseEntity
                .ok()
                .body(toDto(appUserService.findUserById(id)));
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
        return new AppUserDto(appUser.getId(), appUser.getName(), appUser.getSurname(), appUser.getLogin());
    }

}
