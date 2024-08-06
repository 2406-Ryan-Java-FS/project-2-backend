package com.revature.configurations;

import com.revature.filters.JwtAuthenticationFilter;
import com.revature.models.enums.Role;
import com.revature.repositories.UserRepository;
import com.revature.services.JwtServiceImpl;
import com.revature.services.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractAuthenticationFilterConfigurer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtServiceImpl jwtServiceImpl;
    private final UserServiceImpl userService;
    private final UserRepository userRepository;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    public SecurityConfig(JwtServiceImpl jwtServiceImpl, UserServiceImpl userService, UserRepository userRepository, JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtServiceImpl = jwtServiceImpl;
        this.userService = userService;
        this.userRepository = userRepository;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(registry -> {
//                            registry.requestMatchers( "/public/**").permitAll();
                            registry.requestMatchers( "/users","/courses", "/enrollments").hasRole("EDUCATOR");
                            registry.requestMatchers( "/users","/courses").hasRole("STUDENT");
                            registry.anyRequest().authenticated();
                        })
                .formLogin(AbstractAuthenticationFilterConfigurer::permitAll)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        List<SimpleGrantedAuthority> studentAuthorities = Arrays.stream(Role.values())
                .filter(role -> role == Role.student)
                .map(role -> new SimpleGrantedAuthority(role.name()))
                .collect(Collectors.toList());

        List<SimpleGrantedAuthority> educatorAuthorities = Arrays.stream(Role.values())
                .filter(role -> role == Role.educator)
                .map(role -> new SimpleGrantedAuthority(role.name()))
                .collect(Collectors.toList());

        User student = new User("student", passwordEncoder().encode("password"), studentAuthorities);
        User educator = new User("educator", passwordEncoder().encode("password"), educatorAuthorities);

        return new InMemoryUserDetailsManager(student, educator);
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService());
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        return new ProviderManager(authenticationProvider());
    }

//    @Bean
//    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
//        return authenticationConfiguration.getAuthenticationManager();
//    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
