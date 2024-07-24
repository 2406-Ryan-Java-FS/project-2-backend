package com.revature.controllers;

import com.revature.models.SignUpRequest;
import com.revature.models.Student;
import com.revature.models.Educator;
import com.revature.services.UserService;
import com.revature.services.util.JwtUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

public class AuthenticationandRegistration {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;
   
    @PostMapping("/register")
    
    public String registerUser(@RequestBody SignUpRequest signUpRequest) {
        String encodedPassword = passwordEncoder.encode(signUpRequest.getPassword());
        String role = signUpRequest.getRole();

        if (role.equalsIgnoreCase("student")) {
            Student student = new Student();
            student.setEmail(signUpRequest.getEmail());
            student.setPassword(encodedPassword);
            student.setLastName(signUpRequest.getLastName());
            studentRepository.save(student);
            return "Student registered successfully";
        } else if (role.equalsIgnoreCase("educator")) {
            Educator educator = new Educator();
            educator.setEmail(signUpRequest.getEmail());
            educator.setPassword(encodedPassword);
            educator.setLastName(signUpRequest.getLastName());
            educator.setProfessionalDetails(signUpRequest.getProfessionalDetails());
            educatorRepository.save(educator);
            return "Educator registered successfully";
        } else {
            throw new IllegalArgumentException("Invalid role");
        }
    }

    
    @PostMapping("/authenticate")
    public SignInResponse createAuthenticationToken(@RequestBody SignInRequest signInRequest) throws Exception {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(signInRequest.getUsername(), signInRequest.getPassword())
        );

        final User user = userService
                .loadUserByUsername(signInRequest.getUsername());

        final String jwt = jwtUtil.generateToken(user.getUsername(), user.getAuthorities().iterator().next().getAuthority());

        return new SignInResponse(jwt);
    }
}

