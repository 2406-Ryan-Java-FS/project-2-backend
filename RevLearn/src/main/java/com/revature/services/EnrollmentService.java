package com.revature.services;

import java.util.List;

import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.revature.models.Enrollment;
import com.revature.models.PayStatus;

public interface EnrollmentService {
    // Fidel
    List<Enrollment> getAllEnrollments();
    // Jason
    Enrollment registerEnrollment(Enrollment newEnrollment);
    // Chase
    Enrollment getEnrollmentById(Integer theEnrollmentId);      // getting an enrollment object with a specific id
    // Alex
    List<Enrollment> getEnrollmentByStudentId(Integer theStudentId);

    List<Enrollment> getEnrollmentsByCourseId(Integer theCourseId);

    List<Enrollment> getEnrollmentsByStudentIdAndPaymentStatus(Integer theStudentId, PayStatus thePaymentStatus);

    List<Enrollment> getEnrollmentsByPaymentStatus(PayStatus thePaymentStatus);
    // Chase
    Enrollment updateEnrollmentById(Integer theEnrollmentId, PayStatus thePaymentStatus);
    // Fidel
    Enrollment updateEnrollmentById(Integer theEnrollmentId, String theCourseReview);
    // Alex
    Integer deleteEnrollment(Integer theEnrollmentId);
}