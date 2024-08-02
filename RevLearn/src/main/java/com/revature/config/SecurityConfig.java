package com.revature.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/public/**").permitAll() // Allow public access to certain paths
                .requestMatchers("/resources/**").permitAll() // Allow public access to static resources
                .anyRequest().authenticated() // All other requests require authentication
            )
            .formLogin(form -> form
                .loginPage("/login")
                .permitAll() // Allow public access to the login page
            )
            .logout(logout -> logout
                .permitAll() // Allow public access to logout
            )
            .csrf(csrf -> csrf
                .disable() // Disable CSRF if using stateless sessions (e.g., for APIs)
            );

        return http.build();
    }
}

