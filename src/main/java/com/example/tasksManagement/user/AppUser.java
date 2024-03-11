package com.example.tasksManagement.user;


import com.example.tasksManagement.task.Task;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "USERS")
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class AppUser {
    public AppUser(String name, String surname, String login) {
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
    private String login;
    @JsonIgnore
    @OneToMany(mappedBy = "createdBy")
    private List<Task> createdTasks = new ArrayList<>();
    @JsonIgnore
    @OneToMany(mappedBy = "assignedUser")
    private List<Task> assignedTasks = new ArrayList<>();
}
