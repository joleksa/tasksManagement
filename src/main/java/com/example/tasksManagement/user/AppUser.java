package com.example.tasksManagement.user;


import com.example.tasksManagement.task.Task;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "USERS")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class AppUser implements UserDetails {
    public AppUser(String name, String surname, String login) {
        this.name = name;
        this.surname = surname;
        this.login = login;
    }

    public AppUser(Long id,String name, String surname, String login) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.login = login;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;
    @Setter
    private String name;
    @Setter
    private String surname;
    @Setter
    @Column(unique = true)
    private String login;
    private String password;
    //TODO zastapic jsonIgnore
    @JsonIgnore
    @OneToMany(mappedBy = "createdBy", fetch = FetchType.LAZY)
    private List<Task> createdTasks = new ArrayList<>();
    @JsonBackReference
    @OneToMany(mappedBy = "assignedUser", fetch = FetchType.LAZY)
    private List<Task> assignedTasks = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private Role role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return login;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
