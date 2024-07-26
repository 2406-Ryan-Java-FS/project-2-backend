package com.revature.controllers;

import com.revature.DTO.UserEducatorDTO;
import com.revature.exceptions.CustomHttpException;
import com.revature.models.Educator;
import com.revature.models.User;
import com.revature.models.enums.Role;
import com.revature.services.EducatorService;
import com.revature.services.JwtService;
import com.revature.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    UserService userService;
    EducatorService educatorService;
    JwtService jwtService;

    @Autowired
    public UserController(UserService userService, EducatorService educatorService, JwtService jwtService) {
        this.userService = userService;
        this.educatorService = educatorService;
        this.jwtService = jwtService;
    }

    /**
     * Endpoint for registering a new User.
     *
     * @param userEducatorDTO Data containing User fields and optionally Educator fields to be registered.
     * @return A ResponseEntity containing the userId of the newly persisted User, or the educatorId if the User is an Educator, along with a 201 status code on success, or an error message and appropriate status code on failure.
     */
    @PostMapping
    public ResponseEntity<Object> addUser(@RequestBody UserEducatorDTO userEducatorDTO) {

        try {
            User newUser = userService.addUser(userEducatorDTO.extractUser());

            if (newUser.getRole().equals(Role.educator)) {
                Educator extractedEducator = userEducatorDTO.extractEducator();
                extractedEducator.setEducatorId(newUser.getUserId());
                Educator newEducator = educatorService.addEducator(extractedEducator);
                return new ResponseEntity<>(newEducator.getEducatorId(), HttpStatus.CREATED);
            }
            return new ResponseEntity<>(newUser.getUserId(), HttpStatus.CREATED);
        } catch (CustomHttpException e) {
            return new ResponseEntity<>(e.getMessage(), e.getStatus());
        }
    }

    /**
     * Endpoint for verifying a User login.
     *
     * @param user A User containing an email/password combination to be verified.
     * @return A ResponseEntity containing a JWT Token as a string along with a 200 status code on success, or an error message and appropriate status code on failure.
     */
    @PostMapping("/login")
    public ResponseEntity<Object> loginUser(@RequestBody User user) {

        try {
            Integer validUserId = userService.verifyUser(user);
            return new ResponseEntity<>(jwtService.generateJwt(validUserId), HttpStatus.OK);
        } catch (CustomHttpException e) {
            return new ResponseEntity<>(e.getMessage(), e.getStatus());
        }
    }

    /**
     * Endpoint for retrieving the currently logged-in User and Educator information given a valid JWT authorization.
     *
     * @param authorization The JWT authorization token of the currently logged-in User.
     * @return A ResponseEntity containing the User and Educator data (if applicable) along with a 200 status code on success, or an error message and appropriate status code on failure.
     */
    @GetMapping
    public ResponseEntity<Object> getCurrentUser(@RequestHeader String authorization) {

        try {
            User currentUser = jwtService.getUserFromToken(authorization);

            if (currentUser.getRole().equals(Role.educator)) {
                Educator currentEducator = educatorService.getEducator(currentUser.getUserId());
                UserEducatorDTO dto = userService.combineUserAndEducator(currentUser, currentEducator);
                return new ResponseEntity<>(dto, HttpStatus.OK);
            }
            return new ResponseEntity<>(currentUser, HttpStatus.OK);
        } catch (CustomHttpException e) {
            return new ResponseEntity<>(e.getMessage(), e.getStatus());
        }
    }
}
