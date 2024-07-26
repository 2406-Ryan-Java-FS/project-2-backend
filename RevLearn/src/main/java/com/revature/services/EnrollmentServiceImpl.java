package com.revature.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import com.revature.models.Enrollment;
import com.revature.models.PayStatus;
import com.revature.repositories.EnrollmentRepository;
import org.springframework.stereotype.Service;

@Service
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
        Optional<Enrollment> optionalEnrollment = enrollmentRepository.findById(theEnrollmentId);

        if(optionalEnrollment.isPresent()) return optionalEnrollment.get();
        else return null;
    }

    @Override
    public List<Enrollment> getEnrollmentByStudentId(Integer theStudentId) {
        return enrollmentRepository.findByStudentId(theStudentId);
    }

    @Override
    public Enrollment updateEnrollmentById(Integer theEnrollmentId, PayStatus thePaymentStatus) {

        int rowsUpdated = enrollmentRepository.updateEnrollmentPaymentStatusById(theEnrollmentId, thePaymentStatus);

        if(rowsUpdated == 1) return this.getEnrollmentById(theEnrollmentId);
        else return null;
    }

    @Override
    public Enrollment updateEnrollmentById(Integer theEnrollmentId, String theCourseReview) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateEnrollmentById'");
    }

    @Override
    public Integer deleteEnrollment(Integer theEnrollmentId) {
        try {
            enrollmentRepository.deleteById(theEnrollmentId);
            return 1;
        } catch (Exception e) {
            System.err.println("Exception occurred while deleting enrollment: " + e.getMessage());
            return 0;
        }}
    
}
