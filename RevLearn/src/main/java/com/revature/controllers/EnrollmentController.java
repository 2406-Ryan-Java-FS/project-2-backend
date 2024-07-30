package com.revature.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.exceptions.BadRequestException;
import com.revature.models.Enrollment;
import com.revature.models.enums.PayStatus;
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
    @PatchMapping("/enrollments/review/{theEnrollmentId}")
    public ResponseEntity<?> updateEnrollmentById(@PathVariable("theEnrollmentIdCR") Integer theEnrollmentId,
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
     * GET request handler method that will find a record in the Enrollments table
     * with the specified enrollmentId
     * 
     * @param theEnrollmentId
     * @return returns an OK response entity with type of Enrollment if record
     *         exists in the table
     *         returns a NOT_FOUND response entity with a String type displaying
     *         that it could not be found
     */
    @GetMapping("/enrollments/{theEnrollmentId}")
    public ResponseEntity<?> getEnrollmentById(@PathVariable("theEnrollmentId") Integer theEnrollmentId) {

        try {
            Enrollment theEnrollment = enrollmentService.getEnrollmentById(theEnrollmentId);

            return ResponseEntity.ok(theEnrollment);
        } catch (NotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }

    }

    /**
     * GET request handler method that will find all records in the database with
     * the specified course id
     * 
     * @param theCourseId - course id value used as condition to query the database
     * @return A Response Entity with a list of all enrollments with the specified
     *         course id
     */
    @GetMapping("/enrollments/courses/{theCourseId}")
    public ResponseEntity<List<Enrollment>> getEnrollmentsByCourseId(@PathVariable("theCourseId") Integer theCourseId) {
        return ResponseEntity.ok(enrollmentService.getEnrollmentsByCourseId(theCourseId));
    }

    /**
     * GET request handler method that will find all records in the database with
     * the specified studentId and payment status
     * 
     * @param theStudentId - studentId value being used as a condition to query the
     *                     database
     * @param status       - payment status value used as condition to query the
     *                     database
     * @return A response entity of either List<Enrollment> type if no exceptions
     *         are thrown or String type if the status is not valid
     */
    @GetMapping("/enrollments/students/{theStudentId}/{thePaymentStatus}")
    public ResponseEntity<?> getEnrollmentsForStudentWithStatus(@PathVariable("theStudentId") Integer theStudentId,
            @PathVariable("thePaymentStatus") String status) {
        try {
            PayStatus payStatus = PayStatus.valueOf(status);

            return ResponseEntity
                    .ok(enrollmentService.getEnrollmentsByStudentIdAndPaymentStatus(theStudentId, payStatus));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Invalid Status");
        }
    }

    /**
     * GET request handler method that will find all records in the database with
     * the specified payment status
     * 
     * @param status - payment status value used as condition to query the database
     * @return A response entity of either List<Enrollment> type if no exceptions
     *         are thrown or String type if the status is not valid
     */
    @GetMapping("/enrollments/status/{thePaymentStatus}")
    public ResponseEntity<?> getEnrollmentsWithPayStatus(@PathVariable("thePaymentStatus") String status) {
        try {
            PayStatus payStatus = PayStatus.valueOf(status);

            return ResponseEntity.ok(enrollmentService.getEnrollmentsByPaymentStatus(payStatus));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Invalid Status");
        }
    }

    /**
     * Patch request handler that searches for the record with the passed
     * enrollmentId and updates the pay status field from that record
     * 
     * @param theEnrollmentId - primary key value to update a single row in table
     * @param payStatus       - value to be updated must be string type and value
     *                        must be 'pending', 'cancelled', or 'completed'
     * @return returns the updated record from the table
     * @throws BadRequestException
     */
    @PatchMapping("/enrollments/payStatus/{theEnrollmentId}")
    public ResponseEntity<?> updatePaymentStatusForEnrollment(@PathVariable("theEnrollmentId") Integer theEnrollmentId,
            @RequestBody String payStatus) {

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(payStatus);
            String payStatusString = jsonNode.get("payStatus").asText();

            PayStatus status = PayStatus.valueOf(payStatusString);

            return ResponseEntity.ok(enrollmentService.updateEnrollmentById(theEnrollmentId, status));

        } catch (JsonProcessingException | BadRequestException | NullPointerException e) {
            return ResponseEntity.badRequest().body("Could not update payment status");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Please enter 'pending', 'completed', or 'cancelled'");
        } catch (NotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    @PostMapping("/enrollments")
    public ResponseEntity<?> addEnrollment(@RequestBody Enrollment newEnrollment) {
        try {
            Enrollment enrollment = enrollmentService.registerEnrollment(newEnrollment);
            return ResponseEntity.ok(enrollment);
        } catch (BadRequestException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/enrollments/students/{studentId}")
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
