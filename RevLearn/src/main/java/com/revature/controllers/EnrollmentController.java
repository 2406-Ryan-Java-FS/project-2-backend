package com.revature.controllers;

import com.revature.models.Enrollment;
import com.revature.services.EnrollmentServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/enrollments")
public class EnrollmentController {


    @Autowired
    public EnrollmentController(EnrollmentServiceImpl enrollmentServiceImpl) {
        this.enrollmentServiceImpl = enrollmentServiceImpl;
    }

    EnrollmentServiceImpl enrollmentServiceImpl;

    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<Enrollment>> getEnrollmentByStudentId(@PathVariable("studentId") Integer theStudentId) {
        List<Enrollment> enrollments = enrollmentServiceImpl.getEnrollmentByStudentId(theStudentId);
        if (enrollments != null && !enrollments.isEmpty()) {
            return ResponseEntity.ok(enrollments);
        }
        return ResponseEntity.notFound().build();
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Integer> deleteEnrollment(@PathVariable("id") Integer theEnrollmentId) {
        Integer result = enrollmentServiceImpl.deleteEnrollment(theEnrollmentId);
        if (result == 1) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.status(500).body(result);
        }
    }




}
