package com.revature.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.revature.models.Enrollment;
import com.revature.models.PayStatus;
import com.revature.repositories.EnrollmentRepository;

public class EnrollmentServiceImpl implements EnrollmentService  {

    EnrollmentRepository enrollmentRepository;

    @Autowired
    public EnrollmentServiceImpl(EnrollmentRepository enrollmentRepository) {
        this.enrollmentRepository = enrollmentRepository;
    }

    @Override
    public List<Enrollment> getAllEnrollments() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAllEnrollments'");
    }

    @Override
    public Enrollment registerEnrollment() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'registerEnrollment'");
    }

    @Override
    public Enrollment getEnrollmentById(Integer theEnrollmentId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getEnrollmentById'");
    }

    @Override
    public List<Enrollment> getEnrollmentByStudentId(Integer theStudentId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getEnrollmentByStudentId'");
    }

    @Override
    public Enrollment updateEnrollmentById(Integer theEnrollmentId, PayStatus thePaymentStatus) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateEnrollmentById'");
    }

    @Override
    public Enrollment updateEnrollmentById(Integer theEnrollmentId, String theCourseReview) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateEnrollmentById'");
    }

    @Override
    public Integer deleteEnrollment(Integer theEnrollmentId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteEnrollment'");
    }
    
}