package com.example.tasksManagement.user;

import com.example.tasksManagement.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AppUserService {
    private final AppUserRepository appUserRepository;

    @Autowired
    public AppUserService(AppUserRepository appUserRepository) {
        this.appUserRepository = appUserRepository;
    }

    public List<AppUser> getAppUsers() {
        return appUserRepository.findAll();
    }

    public AppUser findUserByLogin(String login) {
        Optional<AppUser> userOptional = appUserRepository.findByLogin(login);
        return userExistsValidation(userOptional);
    }

    public AppUser findUserById(Long id) {
        Optional<AppUser> userOptional = appUserRepository.findById(id);
        return userExistsValidation(userOptional);
    }

    public void addNewUser(AppUser appUser) {
        Optional<AppUser> userOptional = appUserRepository.findByLogin(appUser.getLogin());
        if (userOptional.isPresent()) {
            throw new BusinessException("login is taken, enter another login");
        }
            appUserRepository.save(appUser);
    }

    public void deleteUser(String login) {
        AppUser userOptional = findUserByLogin(login);
        appUserRepository.delete(userOptional);
    }

    private AppUser userExistsValidation(Optional<AppUser> userOptional) {
        if (userOptional.isEmpty()) {
            throw new BusinessException("user doesn't exist");
        }
        return userOptional.get();
    }


}
