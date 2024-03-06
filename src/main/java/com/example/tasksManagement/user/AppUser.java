package com.example.tasksManagement.user;


import com.example.tasksManagement.task.Task;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "USERS")
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class AppUser {
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
    private List<Task> createdTasks;
    @JsonIgnore
    @OneToMany(mappedBy = "assignedUser")
    private List<Task> assignedTasks;
}
