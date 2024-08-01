package com.revature.controllers;

import java.util.List;

import com.revature.models.Review;
import com.revature.models.User;

import org.springframework.beans.factory.annotation.Autowired;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.exceptions.BadRequestException;
import com.revature.models.Enrollment;
import com.revature.models.enums.PayStatus;
import com.revature.services.EnrollmentService;
import com.revature.services.JwtService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import com.revature.exceptions.NotFoundException;
import com.revature.exceptions.UnauthorizedException;

@CrossOrigin
@RestController
public class EnrollmentController {

    /**
     * Get all methods will be protected by Spring Security - if Implemented
     */

    EnrollmentService enrollmentService;

    JwtService jwtService;

    @Autowired
    public EnrollmentController(EnrollmentService enrollmentService, JwtService jwtService) {
        this.enrollmentService = enrollmentService;
        this.jwtService = jwtService;
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
     * Handler to update the review information for an existing enrollment in the
     * database.
     * 
     * @param theEnrollmentId - the ID of the enrollment that we want to update in
     *                        the database
     * @param theReview       - the review data that will be updated in the database
     * @param token           - the authorization token to identify the user making
     *                        the request
     * @return a ResponseEntity containing the updated enrollment or exception
     *         messages upon failure
     */
    @PatchMapping("/enrollments/review/{theEnrollmentId}")
    public ResponseEntity<?> updateEnrollmentById(@PathVariable Integer theEnrollmentId,
            @RequestBody Review theReview, @RequestHeader(name = "Authorization") String token) {
        User user = jwtService.getUserFromToken(token);
        try {
            Enrollment updatedEnrollment = enrollmentService.updateEnrollmentById(theEnrollmentId, theReview, user);
            return ResponseEntity.ok(updatedEnrollment);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    /**
     * GET request handler method that finds a record in the Enrollments table with
     * the specified enrollmentId.
     * 
     * @param theEnrollmentId - the ID of the enrollment to find
     * @param token           - the authorization token to identify the user making
     *                        the request
     * @return a ResponseEntity containing the Enrollment if the record exists, or
     *         an appropriate error message
     */
    @GetMapping("/enrollments/{theEnrollmentId}")
    public ResponseEntity<?> getEnrollmentById(@PathVariable Integer theEnrollmentId,
            @RequestHeader(name = "Authorization") String token) {
        User user = jwtService.getUserFromToken(token);
        try {
            Enrollment theEnrollment = enrollmentService.getEnrollmentById(theEnrollmentId, user);
            return ResponseEntity.ok(theEnrollment);
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    /**
     * Retrieves an enrollment record based on student ID and course ID.
     *
     * @param theStudentId the ID of the student
     * @param theCourseId  the ID of the course
     * @param token        the JWT token for authorization
     * @return a ResponseEntity containing the enrollment record or an error message
     */
    @GetMapping("enrollments/students/{theStudentId}/courses/{theCourseId}")
    public ResponseEntity<?> getEnrollmentByStudentIdAndCourseId(@PathVariable Integer theStudentId,
            @PathVariable Integer theCourseId,
            @RequestHeader(name = "Authorization") String token) {
        User user = jwtService.getUserFromToken(token);
        try {
            return ResponseEntity
                    .ok(enrollmentService.getEnrollmentByStudentIdAndCourseId(theStudentId, theCourseId, user));
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
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
    public ResponseEntity<List<Enrollment>> getEnrollmentsByCourseId(@PathVariable Integer theCourseId) {
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
    public ResponseEntity<?> getEnrollmentsForStudentWithStatus(@PathVariable Integer theStudentId,
            @PathVariable String thePaymentStatus) {
        try {
            PayStatus payStatus = PayStatus.valueOf(thePaymentStatus);

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
    public ResponseEntity<?> getEnrollmentsWithPayStatus(@PathVariable String thePaymentStatus) {
        try {
            PayStatus payStatus = PayStatus.valueOf(thePaymentStatus);

            return ResponseEntity.ok(enrollmentService.getEnrollmentsByPaymentStatus(payStatus));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Invalid Status");
        }
    }

    /**
     * Patch request handler that searches for the record with the passed
     * enrollmentId and updates the pay status field.
     * 
     * @param theEnrollmentId - the primary key value to identify the enrollment to
     *                        update
     * @param payStatus       - a JSON string containing the pay status to be
     *                        updated; must be 'PENDING', 'CANCELLED', or
     *                        'COMPLETED'
     * @param token           - the authorization token to identify the user making
     *                        the request
     * @return a ResponseEntity containing the updated enrollment record or an error
     *         message
     * @throws BadRequestException if the provided pay status is invalid or the
     *                             update fails
     */
    @PatchMapping("/enrollments/payStatus/{theEnrollmentId}")
    public ResponseEntity<?> updatePaymentStatusForEnrollment(@PathVariable Integer theEnrollmentId,
            @RequestBody String payStatus, @RequestHeader(name = "Authorization") String token) {
        User user = jwtService.getUserFromToken(token);
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(payStatus);
            String payStatusString = jsonNode.get("payStatus").asText();

            PayStatus status = PayStatus.valueOf(payStatusString.toUpperCase());

            return ResponseEntity.ok(enrollmentService.updateEnrollmentById(theEnrollmentId, status, user));

        } catch (JsonProcessingException | NullPointerException e) {
            return ResponseEntity.badRequest().body("Invalid JSON format for payment status");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Please enter 'PENDING', 'COMPLETED', or 'CANCELLED'");
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    /**
     * Adds a new enrollment and returns the newly created enrollment object.
     * 
     * @param newEnrollment - the enrollment object to be created
     * @param token         - the authorization token to identify the user making
     *                      the request
     * @return a ResponseEntity containing the newly created enrollment object or an
     *         error message
     */
    @PostMapping("/enrollments")
    public ResponseEntity<?> addEnrollment(@RequestBody Enrollment newEnrollment,
            @RequestHeader(name = "Authorization") String token) {
        User user = jwtService.getUserFromToken(token);
        try {
            Enrollment enrollment = enrollmentService.registerEnrollment(newEnrollment, user);
            return ResponseEntity.ok(enrollment);
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        } catch (BadRequestException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    /**
     * Retrieves enrollments for a specific student by their student ID.
     * 
     * @param theStudentId - the ID of the student whose enrollments are to be
     *                     retrieved
     * @param token        - the authorization token to identify the user making the
     *                     request
     * @return a ResponseEntity containing a list of enrollments if found, or a 404
     *         Not Found status if no enrollments are found
     */
    @GetMapping("/enrollments/students/{theStudentId}")
    public ResponseEntity<?> getEnrollmentByStudentId(@PathVariable Integer theStudentId,
            @RequestHeader(name = "Authorization") String token) {
        User user = jwtService.getUserFromToken(token);
        try {
            List<Enrollment> enrollments = enrollmentService.getEnrollmentByStudentId(theStudentId, user);
            if (enrollments != null && !enrollments.isEmpty()) {
                return ResponseEntity.ok(enrollments);
            }
            return ResponseEntity.notFound().build();
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    /**
     * Deletes an enrollment by its ID.
     * 
     * @param theEnrollmentId - the ID of the enrollment to be deleted
     * @param token           - the authorization token to identify the user making
     *                        the request
     * @return a ResponseEntity with the result of the deletion operation
     */
    @DeleteMapping("/enrollments/{theEnrollmentId}")
    public ResponseEntity<Integer> deleteEnrollment(@PathVariable Integer theEnrollmentId,
            @RequestHeader(name = "Authorization") String token) {
        User user = jwtService.getUserFromToken(token);
        try {
            Integer result = enrollmentService.deleteEnrollment(theEnrollmentId, user);
            if (result == 1) {
                return ResponseEntity.ok(result);
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
            }
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(0);
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(0);
        }
    }
}