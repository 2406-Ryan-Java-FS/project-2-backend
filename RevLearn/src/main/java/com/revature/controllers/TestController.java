package com.revature.controllers;

import com.revature.models.User;
import com.revature.models.dtos.UserToken;
import com.revature.services.JwtServiceImpl;
import com.revature.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class TestController {
    @Autowired
    JwtServiceImpl js;

    @Autowired
    UserService us;

    @PostMapping("/signup")
    public ResponseEntity<User> createUser(@RequestBody User user){
        return ResponseEntity.status(200).body(us.addUser(user));
    }


    @PostMapping("/signin")
    public ResponseEntity<UserToken> signInUser(@RequestBody User user) {
        User u = us.getUser(user.getUserId());
        String jwt = js.generateJwt(u.getUserId());

        UserToken response = new UserToken(jwt,u);

        return ResponseEntity.status(200).body(response);
    }

    @GetMapping("/token")
    public ResponseEntity<User> checkToken(@RequestHeader(name = "Authorization") String token) {
        return ResponseEntity.status(200).body(js.getUserFromToken(token));
    }

}
