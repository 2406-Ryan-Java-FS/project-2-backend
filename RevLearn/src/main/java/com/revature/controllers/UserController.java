package com.revature.controllers;

import com.revature.models.User;
import com.revature.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
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
     * Endpoint for retrieving a User given its userId.
     *
     * @param userId The userId of the User to retrieve.
     * @return The User along with a 200 status code.
     */
    @GetMapping("/{userId}")
    public ResponseEntity<User> getUser(@PathVariable int userId) {

        User existingUser = userService.getUser(userId);
        return new ResponseEntity<>(existingUser, HttpStatus.OK);
    }

    /**
     * Endpoint for verifying a User login.
     *
     * @param user A User containing a email/password combination to be verified.
     * @return The verified account object.
     */
    @PostMapping("/login")
    public ResponseEntity<Object> loginUser(@RequestBody User user) {

        if (userService.verifyUser(user)) {
            return new ResponseEntity<>(true, HttpStatus.OK);
        }
        return new ResponseEntity<>(false, HttpStatus.UNAUTHORIZED);
    }
}
