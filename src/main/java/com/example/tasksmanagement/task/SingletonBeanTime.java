package com.example.tasksmanagement.task;


import org.springframework.stereotype.Component;


import java.time.Clock;
import java.time.LocalDateTime;

@Component
public class SingletonBeanTime {
    private final LocalDateTime creationTime;

    public SingletonBeanTime() {
        this.creationTime = LocalDateTime.now(Clock.systemDefaultZone());
    }

    public LocalDateTime getCreationTime() {
        return creationTime;
    }

    @Override
    public String toString() {
        return "SingletonBeanTime{" +
                "creationTime=" + creationTime +
                '}';
    }
}
