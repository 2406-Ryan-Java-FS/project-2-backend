package com.revature.services;

import java.util.List;
import java.util.Optional;

import com.revature.exceptions.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;

import com.revature.models.Enrollment;
import com.revature.models.PayStatus;
import com.revature.repositories.EnrollmentRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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
    public Enrollment registerEnrollment(Enrollment newEnrollment) {
        // TODO Auto-generated method stub
        // throw new UnsupportedOperationException("Unimplemented method 'registerEnrollment'");
        Enrollment dbEnrollment = enrollmentRepository.save(newEnrollment);
        return dbEnrollment;
        
    }

    /**
     * Queries the Enrollment table and returns the record with the matching enrollmentId
     * Returns null if id does not exist
     * @param theEnrollmentId
     * @return Enrollment
     */
    @Override
    public Enrollment getEnrollmentById(Integer theEnrollmentId) {
        Optional<Enrollment> optionalEnrollment = enrollmentRepository.findById(theEnrollmentId);

        if(optionalEnrollment.isPresent()) return optionalEnrollment.get();
        else throw new BadRequestException("Enrollment Record with ID: " + theEnrollmentId + " could not be found");
    }

    @Override
    public List<Enrollment> getEnrollmentByStudentId(Integer theStudentId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getEnrollmentByStudentId'");
    }

    @Override
    public Enrollment updateEnrollmentById(Integer theEnrollmentId, PayStatus thePaymentStatus) {

        int rowsUpdated = enrollmentRepository.updateEnrollmentPaymentStatusById(theEnrollmentId, thePaymentStatus);

        if(rowsUpdated == 1) return this.getEnrollmentById(theEnrollmentId);
        else throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Could not update Payment Status");
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
