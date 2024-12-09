package com.example.tasksmanagement.user;

import java.util.Set;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@AllArgsConstructor
@Getter
public enum Role {
    USER(Set.of(
            Permission.USER_READ,
            Permission.USER_UPDATE
    )),
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