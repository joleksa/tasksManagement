package com.example.tasksmanagement.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
public enum Role {
    USER(Collections.emptySet()),
    EDITOR(Set.of(
            Permission.EDITOR_READ,
            Permission.EDITOR_UPDATE,
            Permission.EDITOR_CREATE,
            Permission.EDITOR_DELETE
    )),
    ADMIN(Set.of(
            Permission.EDITOR_READ,
            Permission.EDITOR_UPDATE,
            Permission.EDITOR_CREATE,
            Permission.EDITOR_DELETE,
            Permission.ADMIN_CREATE,
            Permission.ADMIN_DELETE,
            Permission.ADMIN_UPDATE,
            Permission.ADMIN_READ
    ));

    @Getter
    private final Set<Permission> permissions;

    public Set<SimpleGrantedAuthority> getAuthorities() {
        var authorities = getPermissions()
                .stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toSet());
        authorities.add(new SimpleGrantedAuthority("ROLE_" + name()));
        return authorities;
    }
}