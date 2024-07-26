package com.revature.controllers;

import com.revature.models.Educator;
import com.revature.models.User;
import com.revature.services.EducatorService;
import com.revature.services.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/educators")
public class EducatorController {

    EducatorService educatorService;
    JwtService jwtService;

    @Autowired
    public EducatorController(EducatorService educatorService, JwtService jwtService) {
        this.educatorService = educatorService;
        this.jwtService = jwtService;
    }

    /**
     * Endpoint for registering a new Educator.
     *
     * @param educator The Educator to be registered.
     * @return The persisted Educator.
     */
    @PostMapping
    public ResponseEntity<Educator> addEducator(@RequestBody Educator educator) {

        Educator newEducator = educatorService.addEducator(educator);
        return new ResponseEntity<>(newEducator, HttpStatus.CREATED);
    }

//    /**
//     * Endpoint for retrieving an Educator given its educatorId.
//     *
//     * @param educatorId The educatorId of the Educator to retrieve.
//     * @return The Educator along with a 200 status code.
//     */
//    @GetMapping("/{educatorId}")
//    public ResponseEntity<Educator> getEducator(@PathVariable Integer educatorId) {
//
//        Educator existingEducator = educatorService.getEducator(educatorId);
//        return new ResponseEntity<>(existingEducator, HttpStatus.OK);
//    }

    /**
     * Endpoint for retrieving the currently logged-in Educator given a valid JWT token.
     *
     * @param token The JWT token of the currently logged-in Educator.
     * @return The User along with a 200 status code.
     */
    @GetMapping
    public ResponseEntity<Educator> getCurrentEducator(@PathVariable String token) {

        User currentUser = jwtService.getUserFromToken(token);
        Educator currentEducator = educatorService.getEducator(currentUser.getUserId());
        return new ResponseEntity<>(currentEducator, HttpStatus.OK);
    }
}
