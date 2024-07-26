package com.revature.controllers;

import com.revature.exceptions.UnauthorizedException;
import com.revature.models.User;
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
    JwtService jwtService;

    @Autowired
    public UserController(UserService userService, JwtService jwtService) {
        this.userService = userService;
        this.jwtService = jwtService;
    }

    /**
     * Endpoint for registering a new User.
     *
     * @param user The User to be registered.
     * @return The persisted User with its newly assigned userId.
     */
    @PostMapping
    public ResponseEntity<User> addUser(@RequestBody User user) {

        User newUser = userService.addUser(user);
        return new ResponseEntity<>(newUser, HttpStatus.CREATED);
    }

    /**
     * Endpoint for verifying a User login.
     *
     * @param user A User containing a email/password combination to be verified.
     * @return A JWT Token as a string.
     */
    @PostMapping("/login")
    public ResponseEntity<Object> loginUser(@RequestBody User user) {

        try {
            Integer validUserId = userService.verifyUser(user);
            return new ResponseEntity<>(jwtService.generateJwt(validUserId), HttpStatus.OK);
        } catch (UnauthorizedException ue) {
            return new ResponseEntity<>(ue.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }

    /**
     * Endpoint for retrieving the currently logged-in User given a valid JWT authorization.
     *
     * @param authorization The JWT authorization of the currently logged-in User.
     * @return The User along with a 200 status code.
     */
    @GetMapping
    public ResponseEntity<User> getCurrentUser(@RequestHeader String authorization) {

        User currentUser = jwtService.getUserFromToken(authorization);
        return new ResponseEntity<>(currentUser, HttpStatus.OK);
    }
}
