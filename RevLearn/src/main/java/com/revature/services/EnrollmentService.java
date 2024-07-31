package com.revature.services;

import java.util.List;

import com.revature.models.Enrollment;
import com.revature.models.Review;
import com.revature.models.enums.PayStatus;

public interface EnrollmentService {
    List<Enrollment> getAllEnrollments();

    Enrollment registerEnrollment(Enrollment newEnrollment);

    Enrollment getEnrollmentById(Integer theEnrollmentId); // getting an enrollment object with a specific id

    List<Enrollment> getEnrollmentByStudentId(Integer theStudentId);

    List<Enrollment> getEnrollmentsByCourseId(Integer theCourseId);

    List<Enrollment> getEnrollmentsByStudentIdAndPaymentStatus(Integer theStudentId, PayStatus thePaymentStatus);

    List<Enrollment> getEnrollmentsByPaymentStatus(PayStatus thePaymentStatus);

    Enrollment updateEnrollmentById(Integer theEnrollmentId, PayStatus thePaymentStatus);

    Enrollment updateEnrollmentById(Integer theEnrollmentId, Review review);

    Integer deleteEnrollment(Integer theEnrollmentId);
}