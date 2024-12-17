package com.example.tasksmanagement.task;

import java.time.Clock;
import java.time.LocalDateTime;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

@Component
@Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class RequestBeanTime {
    private final LocalDateTime creationTime;


    public RequestBeanTime() {
        this.creationTime = LocalDateTime.now(Clock.systemDefaultZone());
    }

    public LocalDateTime getCreationTime() {
        return creationTime;
    }

    @Override
    public String toString() {
        return "RequestBeanTime{" +
                "creationTime=" + creationTime +
                '}';
    }
}
