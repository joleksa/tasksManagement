package com.example.tasksmanagement.config;


import com.example.tasksmanagement.user.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(HttpMethod.POST,"/api/auth/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/task/**").hasAnyRole("USER","ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/task/**").hasAnyRole(Role.ADMIN.name(), Role.EDITOR.name())
                        .requestMatchers(HttpMethod.PATCH, "/api/task/**").hasAnyRole(Role.ADMIN.name(), Role.EDITOR.name())
                        .requestMatchers(HttpMethod.DELETE, "/api/task/**").hasRole(Role.ADMIN.name())
                        .requestMatchers(HttpMethod.GET, "/api/user/**").hasAnyRole(Role.USER.name(), Role.ADMIN.name(), Role.EDITOR.name())
                        .requestMatchers(HttpMethod.POST, "/api/user/**").hasAnyRole(Role.ADMIN.name(), Role.EDITOR.name())
                        .requestMatchers(HttpMethod.PATCH, "/api/user/**").hasAnyRole(Role.ADMIN.name(), Role.EDITOR.name())
                        .requestMatchers(HttpMethod.DELETE, "/api/user/**").hasRole(Role.ADMIN.name())
                        .anyRequest()
                        .authenticated())
                .sessionManagement(sessionManagementConfigurer -> sessionManagementConfigurer
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}