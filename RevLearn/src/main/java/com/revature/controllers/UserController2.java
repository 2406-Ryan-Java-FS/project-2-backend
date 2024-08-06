package com.revature.controllers;

import com.revature.exceptions.NotFoundException;
import com.revature.models.*;
import com.revature.models.dtos.Body;
import com.revature.models.dtos.UserEducator;
import com.revature.repositories.UserRepository;
import com.revature.services.EducatorServiceImpl;
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
    @Autowired EducatorServiceImpl educatorService;
    @Autowired UserRepository userRepository;

    @PostMapping("/users2/signup")
    public ResponseEntity<UserEducator> signup(@RequestBody UserEducator dtoIn) {
        logger.info("dtoIn="+ Help.json(dtoIn,true,true));

        //Confusing data models
        User newUser=userService.addUser(dtoIn.getUser());//creates new userId
        dtoIn.getEducator().setEducatorId(newUser.getUserId());//make sure userId and educactorId are the same so things work
        Educator newEdu=educatorService.addEducator(dtoIn.getEducator());//creates educator

        UserEducator dtoBack = new UserEducator(null, newUser,newEdu);
        logger.info("dtoBack="+ Help.json(dtoBack,true,true));

        return new ResponseEntity<>(dtoBack, HttpStatus.CREATED);
    }

    @PostMapping("/users2/signin")
    public ResponseEntity<Object> signin(@RequestBody UserEducator dtoIn) {
        logger.info("dtoIn="+ Help.json(dtoIn,true,true));

        if(userRepository.findByEmail(dtoIn.getUser().getEmail())==null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("User with email "+dtoIn.getUser().getEmail()+" doesn't exist");
            //throw new NotFoundException("User with email "+dtoIn.getUser().getEmail()+" doesn't exist");
        }

        Integer validUserId = userService.verifyUser(dtoIn.getUser());//dto only contains email and password
        String jwt = jwtService.generateJwt(validUserId);

        UserEducator dtoBack=new UserEducator(jwt,
            userService.getUser(validUserId),
            educatorService.getEducator(validUserId));//userId and educatorId are the same I guess

        logger.info("dtoBack="+ Help.json(dtoBack,true,true));

        return new ResponseEntity<>(dtoBack, HttpStatus.OK);
    }

    @PostMapping("/users2/signout")
    public ResponseEntity<Body> signout(@RequestHeader String token) {
        logger.info("token="+ StringUtils.truncate(token,30));

        User currentUser = jwtService.getUserFromToken(token);
        logger.info("currentUser="+ Help.json(currentUser,true,true));

        return new ResponseEntity<>(new Body("This is not implemented in the backend"), HttpStatus.OK);
    }
}
