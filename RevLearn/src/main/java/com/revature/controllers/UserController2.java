package com.revature.controllers;

import com.revature.models.*;
import com.revature.models.dtos.UserEducator;
import com.revature.models.dtos.UserToken;
import com.revature.services.JwtService;
import com.revature.services.UserService;
import com.revature.util.Help;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController2 {

    private static final Logger logger= LoggerFactory.getLogger(UserController2.class);

    @Autowired UserService userService;
    @Autowired JwtService jwtService;

    @PostMapping("/users2/signup")
    public ResponseEntity<UserEducator> signup(@RequestBody UserEducator dtoIn) {
        logger.info("dtoIn="+ Help.json(dtoIn,true,true));

        UserEducator dtoBack = new UserEducator(null,userService.addUser(dtoIn.getUser()),null);
        logger.info("dtoBack="+ Help.json(dtoBack,true,true));

        return new ResponseEntity<>(dtoBack, HttpStatus.CREATED);
    }

    @PostMapping("/users2/signin")
    public ResponseEntity<UserEducator> signin(@RequestBody UserEducator dtoIn) {
        logger.info("dtoIn="+ Help.json(dtoIn,true,true));

        Integer validUserId = userService.verifyUser(dtoIn.getUser());
        String jwt = jwtService.generateJwt(validUserId);

        UserEducator dtoBack=new UserEducator(jwt, userService.getUser(validUserId),null);
        logger.info("dtoBack="+ Help.json(dtoBack,true,true));

        return new ResponseEntity<>(dtoBack, HttpStatus.OK);
    }

    @PostMapping("/users2/signout")
    public ResponseEntity<String> signout(@RequestHeader String token) {
        logger.info("token="+ StringUtils.truncate(token,30));

        User currentUser = jwtService.getUserFromToken(token);
        logger.info("currentUser="+ Help.json(currentUser,true,true));

        return new ResponseEntity<>("This is not implemented in the backend", HttpStatus.OK);
    }
}
