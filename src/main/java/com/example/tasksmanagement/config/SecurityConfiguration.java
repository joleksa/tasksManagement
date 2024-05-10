package com.example.tasksmanagement.config;


import com.example.tasksmanagement.user.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static com.example.tasksmanagement.user.Permission.*;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfiguration {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(HttpMethod.POST,"/api/auth/**").permitAll()
                        //.requestMatchers( "/api/task/**").hasAnyRole(Role.USER.name(), Role.ADMIN.name(), Role.EDITOR.name())
                        .requestMatchers(HttpMethod.GET, "/api/task/**").hasAnyRole(Role.USER.name(), Role.ADMIN.name(), Role.EDITOR.name())
                        .requestMatchers(HttpMethod.POST, "/api/task/**").hasAnyAuthority(ADMIN_CREATE.name(), EDITOR_CREATE.name())
                        .requestMatchers(HttpMethod.PATCH, "/api/task/**").hasAnyAuthority(ADMIN_UPDATE.name(), EDITOR_UPDATE.name())
                        .requestMatchers(HttpMethod.DELETE, "/api/task/**").hasAnyAuthority(ADMIN_DELETE.name())
                        .requestMatchers( "/api/user/**").hasAnyRole(Role.USER.name(), Role.ADMIN.name(), Role.EDITOR.name())
                        .requestMatchers(HttpMethod.GET, "/api/user/**").hasAnyRole(Role.USER.name(), Role.ADMIN.name(), Role.EDITOR.name())
                        .requestMatchers(HttpMethod.POST, "/api/user/**").hasAnyAuthority(ADMIN_CREATE.name())
                        .requestMatchers(HttpMethod.PATCH, "/api/user/**").hasAnyAuthority(ADMIN_UPDATE.name())
                        .requestMatchers(HttpMethod.DELETE, "/api/user/**").hasAnyAuthority(ADMIN_DELETE.name())
                        .anyRequest()
                        .authenticated())
                .sessionManagement(sessionManagementConfigurer -> sessionManagementConfigurer
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}