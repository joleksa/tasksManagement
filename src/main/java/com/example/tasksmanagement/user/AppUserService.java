package com.example.tasksmanagement.user;

import com.example.tasksmanagement.BusinessException;
import com.example.tasksmanagement.dto.AppUserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
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

    public Page<AppUserDto> getAllUsersSortedAndPaginated(int pageNo, int pageSize, String field, String direction) {
        Page<AppUser> users = getPageableUsers(pageNo, pageSize, field, direction);
        List<AppUserDto> listAppUserDto = users.getContent().stream().map(this::toDto).toList();
        return new PageImpl<>(listAppUserDto, users.getPageable(), users.getTotalElements());
    }

    public Page<AppUser> getPageableUsers(int pageNo, int pageSize, String field, String direction) {
        Pageable pageable = PageRequest.of(pageNo, pageSize).withSort(Sort.Direction.fromString(direction),field);
        return appUserRepository.findAll(pageable);
    }

    public AppUser findUserByLogin(String login) {
        Optional<AppUser> userOptional = appUserRepository.findByLogin(login);
        return userExistsValidation(userOptional);
    }

    public AppUser findUserById(Long id) {
        Optional<AppUser> userOptional = appUserRepository.findById(id);
        return userExistsValidation(userOptional);
    }

    public void deleteUser(String login) {
        AppUser user = findUserByLogin(login);
        appUserRepository.delete(user);
    }

    private AppUser userExistsValidation(Optional<AppUser> userOptional) {
        if (userOptional.isEmpty()) {
            throw new BusinessException("user doesn't exist");
        }
        return userOptional.get();
    }

    AppUserDto toDto(AppUser appUser) {
        return new AppUserDto(appUser.getId(), appUser.getName(), appUser.getSurname(), appUser.getLogin());
    }



}
