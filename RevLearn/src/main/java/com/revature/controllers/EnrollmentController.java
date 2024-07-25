package com.revature.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.exceptions.BadRequestException;
import com.revature.models.Enrollment;
import com.revature.models.PayStatus;
import com.revature.services.EnrollmentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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


    @PatchMapping("/enrollments/{id}")
    public ResponseEntity<?> updatePaymentStatusForEnrollment(@PathVariable("id") Integer theEnrollmentId, @RequestBody String payStatus){

        try{
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(payStatus);
            String payStatusString = jsonNode.get("payStatus").asText();

            PayStatus status = PayStatus.valueOf(payStatusString);

            return ResponseEntity.ok(enrollmentService.updateEnrollmentById(theEnrollmentId, status));

        } catch (JsonMappingException e) {
            throw new BadRequestException("Could not complete update request");
        } catch (JsonProcessingException e) {
            throw new BadRequestException("Could not complete update request");
        } catch (IllegalArgumentException e){
            throw new BadRequestException("Please enter 'pending', 'completed', or 'cancelled'");
        }
    }
}
