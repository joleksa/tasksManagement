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

    public AppUser findUser(String login) {
        Optional<AppUser> userOptional = appUserRepository.findByLogin(login);
        if (userOptional.isEmpty()) {
            throw new BusinessException("user doesn't exist");
        }
        return userOptional.get();
    }

    public void addNewUser(AppUser appUser) {
        Optional<AppUser> userOptional = appUserRepository.findByLogin(appUser.getLogin());
        if (userOptional.isPresent()) {
            throw new BusinessException("login is taken, enter another login");
        }
            appUserRepository.save(appUser);
    }

    public void deleteUser(String login) {
        AppUser userOptional = findUser(login);
        appUserRepository.delete(userOptional);
    }


}
