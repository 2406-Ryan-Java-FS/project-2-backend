package com.revature.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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


import com.revature.exceptions.NotFoundException;

@RestController
public class EnrollmentController {

    EnrollmentService enrollmentService;

    @Autowired
    public EnrollmentController(EnrollmentService enrollmentService) {
        this.enrollmentService = enrollmentService;
    }

    /**
     * handler to get all enrollments.
     * 
     * @return List<Enrollment> a list of enrollment objects wrapped in a
     *         ResponseEntity as the response body to the HTTP caller.
     *         Note: This method catches and handles RuntimeException if something
     *         goes wrong with the HTTP call.
     */
    @GetMapping("/enrollments")
    public ResponseEntity<?> getAllEnrollments() {
        try {
            List<Enrollment> allEnrollments = enrollmentService.getAllEnrollments();
            return ResponseEntity.ok(allEnrollments);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }






    /**
     * handler to update any information for an existing user in the database
     * 
     * @param theEnrollmentId - the id of the course that we want to update in the
     *                        database
     * @param theCourseReview the review data that we will update in the database
     * @return a response entity containing the updated course or exception messages
     *         upon failure
     */
    @PatchMapping("/enrollments/{theEnrollmentId}")
    public ResponseEntity<?> updateEnrollmentById(@PathVariable Integer theEnrollmentId,
            @RequestBody String theCourseReview) {
        try {
            Enrollment updatedEnrollment = enrollmentService.updateEnrollmentById(theEnrollmentId, theCourseReview);
            return ResponseEntity.ok(updatedEnrollment);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    /**
     *  GET request handler method that will find a record in the Enrollments table with the specified enrollmentId
     * @param theEnrollmentId
     * @return returns an OK response entity with type of Enrollment if record exists in the table
     * returns a NOT_FOUND response entity with a String type displaying that it could not be found
     */
    @GetMapping("/enrollments/{theEnrollmentId}")
    public ResponseEntity<?> getEnrollmentById(@PathVariable("theEnrollmentId") Integer theEnrollmentId) {


        try {
            Enrollment theEnrollment = enrollmentService.getEnrollmentById(theEnrollmentId);

            return ResponseEntity.ok(theEnrollment);
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    /**
     * Patch request handler that searches for the record with the passed enrollmentId and updates the pay status field from that record
     * @param theEnrollmentId - primary key value to update a single row in table
     * @param payStatus - value to be updated must be string type and value must be 'pending', 'cancelled', or 'completed'
     * @return returns the updated record from the table
     * @throws BadRequestException
     */
    @PatchMapping("/enrollments/{theEnrollmentId}")
    public ResponseEntity<?> updatePaymentStatusForEnrollment(@PathVariable("theEnrollmentId") Integer theEnrollmentId,
            @RequestBody String payStatus) {

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(payStatus);
            String payStatusString = jsonNode.get("payStatus").asText();

            PayStatus status = PayStatus.valueOf(payStatusString);

            return ResponseEntity.ok(enrollmentService.updateEnrollmentById(theEnrollmentId, status));

        } catch (JsonMappingException e) {
            throw new BadRequestException("Could not complete update request");
        } catch (JsonProcessingException e) {
            throw new BadRequestException("Could not complete update request");
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Please enter 'pending', 'completed', or 'cancelled'");
        }
    }

    @PostMapping("/enrollments")
    public ResponseEntity<?> addEnrollment(@RequestBody Enrollment newEnrollment){
        try{
            Enrollment enrollment = enrollmentService.registerEnrollment(newEnrollment);
            return ResponseEntity.ok(enrollment);
        }catch (BadRequestException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }


    @GetMapping("/enrollments/{studentId}")
    public ResponseEntity<List<Enrollment>> getEnrollmentByStudentId(@PathVariable("studentId") Integer theStudentId) {
        List<Enrollment> enrollments = enrollmentService.getEnrollmentByStudentId(theStudentId);
        if (enrollments != null && !enrollments.isEmpty()) {
            return ResponseEntity.ok(enrollments);
        }
        return ResponseEntity.notFound().build();
    }


    @DeleteMapping("/enrollments/{id}")
    public ResponseEntity<Integer> deleteEnrollment(@PathVariable("id") Integer theEnrollmentId) {
        Integer result = enrollmentService.deleteEnrollment(theEnrollmentId);
        if (result == 1) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.status(500).body(result);
        }
    }
}
