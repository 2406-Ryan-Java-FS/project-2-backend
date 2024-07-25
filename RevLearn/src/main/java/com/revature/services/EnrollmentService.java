package com.revature.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.revature.models.Enrollment;
import com.revature.models.PayStatus;

@Service
public interface EnrollmentService {
    List<Enrollment> getAllEnrollments();
    Enrollment registerEnrollment();
    Enrollment getEnrollmentById(Integer theEnrollmentId);      // getting an enrollment object with a specific id
    List<Enrollment> getEnrollmentByStudentId(Integer theStudentId);
    Enrollment updateEnrollmentById(Integer theEnrollmentId, PayStatus thePaymentStatus);
    Enrollment updateEnrollmentById(Integer theEnrollmentId, String theCourseReview);
    Integer deleteEnrollment(Integer theEnrollmentId);
}