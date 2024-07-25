package com.revature.controllers;


import com.revature.models.dto.*;
import com.revature.repositories.EducatorRepository;
import com.revature.repositories.StudentRepository;
import com.revature.models.Users;
import com.revature.services.UserService;
import com.revature.services.JwtService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.http.ResponseEntity;
import jakarta.validation.Valid;
import org.javatuples.Pair;


@Validated
@RestController
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    JwtService jwtService;

  
    /*
     * test endpoints.
     */

public class AuthenticationandRegistration {


    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private EducatorRepository educatorRepository;
    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;
   
    @PostMapping("/register")
    
    public String registerUser(@RequestBody Users user) {
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        String role = user.getRole();

        if (role.equalsIgnoreCase("student")) {
            Users student = new Users();
            student.setEmail(user.getEmail());
            student.setPassword(encodedPassword);
            student.setLastName(user.getLastName());
            ((CrudRepository<Users, Integer>) studentRepository).save(student);
            return "Student registered successfully";
        } else if (role.equalsIgnoreCase("educator")) {
            Users educator = new Users();
            educator.setEmail(user.getEmail());
            educator.setPassword(encodedPassword);
            educator.setLastName(user.getLastName());
            educator.setProfessionalDetails(user.getProfessionalDetails());
            ((CrudRepository<Users, Integer>) educatorRepository).save(educator);
            return "Educator registered successfully";
        } else {
            throw new IllegalArgumentException("Invalid role");
        }
    }

    
    
@PostMapping("users/signup")
public ResponseEntity<SignUpOutput> signUpUser(@RequestBody Users user)
{
  
    return ResponseEntity.status(200).body(userService.signUpUser(user));
}

@PostMapping("users/signin")
public ResponseEntity<String> signInUser(@RequestBody @Valid SignInInput signInInput)
{
  
    Pair<Integer, String> response = userService.signInUser(signInInput);
	return ResponseEntity.status(response.getValue0()).body(response.getValue1());
}
}
}



