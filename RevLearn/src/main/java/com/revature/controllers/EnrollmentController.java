package com.revature.controllers;

import com.revature.exceptions.BadRequestException;
import com.revature.models.Enrollment;
import com.revature.services.EnrollmentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class EnrollmentController {

    EnrollmentService enrollmentService;

    public EnrollmentController(EnrollmentService enrollmentService){
        this.enrollmentService = enrollmentService;
    }

    





    @GetMapping("/enrollments/{id}")
    public ResponseEntity<?> getEnrollmentById(@PathVariable("id") Integer theEnrollmentId){

        try{
            Enrollment theEnrollment = enrollmentService.getEnrollmentById(theEnrollmentId);

            return ResponseEntity.ok(theEnrollment);
        } catch (BadRequestException e){
           return ResponseEntity.badRequest().body(e.getMessage());
        }

    }
}
