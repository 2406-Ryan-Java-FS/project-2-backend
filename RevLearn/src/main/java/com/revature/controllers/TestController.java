package com.revature.controllers;

import com.revature.app.RevLearnApplication;
import com.revature.models.User;
import com.revature.repositories.UserRepository;
import com.revature.services.JwtServiceImpl;
import com.revature.services.UserService;
import com.revature.util.Help;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Profile("h2")
@RestController
public class TestController {

    private static final Logger logger= LoggerFactory.getLogger(TestController.class);

    @Autowired JwtServiceImpl js;
    @Autowired UserService us;
    @Autowired UserRepository ur;

    @PostMapping("/signup")
    public ResponseEntity<User> createUser(@RequestBody User user){
        logger.info("Incoming user="+ Help.json(user,true,false));
        return ResponseEntity.status(200).body(us.addUser(user));
    }


    @PostMapping("/signin")
    public ResponseEntity<User> signInUser(
        @RequestHeader("email") String email,
        @RequestHeader("password") String password)
    {
        User loggedInUser = new User();//us.getUser(email,password);
        //loggedInUser.setJwt(js.generateJwt(loggedInUser.getUserId()));
        return ResponseEntity.status(200).body(loggedInUser);
    }

    @GetMapping("/token")
    public ResponseEntity<User> checkToken(@RequestHeader(name = "Authorization") String token) {
        return ResponseEntity.status(200).body(js.getUserFromToken(token));
    }

    @Profile("h2")
    @DeleteMapping("/clear-all")
    public ResponseEntity<String> clear(){
        ur.deleteAll();
        return ResponseEntity.status(200).body("Cleared all database tables");
    }
}
