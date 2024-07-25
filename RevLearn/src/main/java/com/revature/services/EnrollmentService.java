package com.revature.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.revature.models.Enrollment;
import com.revature.models.PayStatus;

public interface EnrollmentService {
    // Fidel
    List<Enrollment> getAllEnrollments();
    // Jason
    Enrollment registerEnrollment();
    // Chase
    Enrollment getEnrollmentById(Integer theEnrollmentId);      // getting an enrollment object with a specific id
    // Alex
    List<Enrollment> getEnrollmentByStudentId(Integer theStudentId);
    // Chase
    Enrollment updateEnrollmentById(Integer theEnrollmentId, PayStatus thePaymentStatus);
    // Fidel
    Enrollment updateEnrollmentById(Integer theEnrollmentId, String theCourseReview);
    // Alex
    Integer deleteEnrollment(Integer theEnrollmentId);
}