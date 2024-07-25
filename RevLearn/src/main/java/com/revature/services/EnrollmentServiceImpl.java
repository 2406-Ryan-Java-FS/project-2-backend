package com.revature.services;

import java.util.List;
import java.util.Optional;

import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;

import com.revature.models.Enrollment;
import com.revature.models.PayStatus;
import com.revature.repositories.EnrollmentRepository;

@Service
public class EnrollmentServiceImpl implements EnrollmentService  {

    EnrollmentRepository enrollmentRepository;

    public EnrollmentServiceImpl(EnrollmentRepository enrollmentRepository) {
        this.enrollmentRepository = enrollmentRepository;
    }

    /**
     * retrieves all enrollments from the repository.
     * @return List<Enrollment> - a list of all Enrollment entities.
     * @throws RuntimeExcepton - if the retrieval operation fails.
     */
    @Override
    public List<Enrollment> getAllEnrollments() {
        try 
        {
            List<Enrollment> allEnrollments = enrollmentRepository.findAll();
            return allEnrollments;
        } 
        catch (Exception e) 
        {
            throw new RuntimeException("Error fetching enrollments: " + e.getMessage());
        }
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
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getEnrollmentByStudentId'");
    }

    @Override
    public Enrollment updateEnrollmentById(Integer theEnrollmentId, PayStatus thePaymentStatus) {

        int rowsUpdated = enrollmentRepository.updateEnrollmentPaymentStatusById(theEnrollmentId, thePaymentStatus);

        if(rowsUpdated == 1) return this.getEnrollmentById(theEnrollmentId);
        else return null;
    }

    /**
     * allows a student to update or put a review for their enrolled course
     * @param theEnrollmentId the id of the enrollment that we want to update
     * @param theCourseReview the review of that paticular course for that particular enrollment
     * @return Enrollment - the updated Enrollment object if the update is successful
     * @throws InvalidItemException if the item update does not succeed
     */
    @Override
    public Enrollment updateEnrollmentById(Integer theEnrollmentId, String theCourseReview) {
        Enrollment theNewEnrollment = null;

        // check if the enrollment already exists in the database
        Optional<Enrollment> dBEnrollmentOptional = enrollmentRepository.findById(theEnrollmentId);

        if(!dBEnrollmentOptional.isPresent())
        {
            throw new BadRequestException()
        }

        // since it exists, please get the enrollment
        Enrollment dBEnrollment = dBEnrollmentOptional.get();

        return dBEnrollment;
    }

    @Override
    public Integer deleteEnrollment(Integer theEnrollmentId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteEnrollment'");
    }
}