package com.revature.services;

import java.util.List;

import com.revature.models.Enrollment;
import com.revature.models.Review;
import com.revature.models.User;
import com.revature.models.enums.PayStatus;

public interface EnrollmentService {
    List<Enrollment> getAllEnrollments();

    Enrollment registerEnrollment(Enrollment newEnrollment, User user);

    Enrollment getEnrollmentById(Integer theEnrollmentId, User user); // getting an enrollment object with a specific id

    Enrollment getEnrollmentByStudentIdAndCourseId(Integer theStudentId, Integer theCourseId, User user);

    List<Enrollment> getEnrollmentByStudentId(Integer theStudentId, User user);

    List<Enrollment> getEnrollmentsByCourseId(Integer theCourseId);

    List<Enrollment> getEnrollmentsByStudentIdAndPaymentStatus(Integer theStudentId, PayStatus thePaymentStatus);

    List<Enrollment> getEnrollmentsByPaymentStatus(PayStatus thePaymentStatus);

    Enrollment updateEnrollmentById(Integer theEnrollmentId, PayStatus thePaymentStatus, User user);

    Enrollment updateEnrollmentById(Integer theEnrollmentId, Review review, User user);

    Integer deleteEnrollment(Integer theEnrollmentId, User user);
}