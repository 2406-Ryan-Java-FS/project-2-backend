package com.revature.services;

import java.util.List;
import java.util.Optional;

import com.revature.exceptions.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;

import com.revature.exceptions.BadRequestException;
import com.revature.exceptions.NotFoundException;
import com.revature.models.Enrollment;
import com.revature.models.PayStatus;
import com.revature.repositories.EnrollmentRepository;
import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class EnrollmentServiceImpl implements EnrollmentService {

    EnrollmentRepository enrollmentRepository;

    @Autowired
    public EnrollmentServiceImpl(EnrollmentRepository enrollmentRepository) {
        this.enrollmentRepository = enrollmentRepository;
    }

    /**
     * retrieves all enrollments from the repository.
     * 
     * @return List<Enrollment> - a list of all Enrollment entities.
     * @throws RuntimeException - if the retrieval operation fails.
     */
    @Override
    public List<Enrollment> getAllEnrollments() {
        try {
            List<Enrollment> allEnrollments = enrollmentRepository.findAll();
            return allEnrollments;
        } catch (Exception e) {
            throw new RuntimeException("Error fetching enrollments: " + e.getMessage());
        }
    }

    /**
     *  This method takes in a new enrollment oject
     * returns the new enrollment object
     * 
     * @param 
     * @return object
     */
    @Override
    public Enrollment registerEnrollment(Enrollment newEnrollment) {
        
        if(newEnrollment.getPaymentStatus() == null){
            throw new BadRequestException("Please enter either 'pending', 'completed', or 'conselled' for payment status.");
        }

        Enrollment dbEnrollment = enrollmentRepository.save(newEnrollment);
        return dbEnrollment;
        
    }

    /**
     *  Service layer method that will find a record in the Enrollments table with the specified enrollmentId
     * @param theEnrollmentId
     * @return returns an Enrollment object if record exists in the table
     * @throws BadRequestException
     */
    @Override
    public Enrollment getEnrollmentById(Integer theEnrollmentId) {
        Optional<Enrollment> optionalEnrollment = enrollmentRepository.findById(theEnrollmentId);

        if (optionalEnrollment.isPresent())
            return optionalEnrollment.get();
        else
            throw new BadRequestException("Enrollment Record with ID: " + theEnrollmentId + " could not be found");
    }

    @Override
    public List<Enrollment> getEnrollmentByStudentId(Integer theStudentId) {
        return enrollmentRepository.findByStudentId(theStudentId);
    }

    /**
     * Service method that will find all records in the database with the specified courseId
     * @param theCourseId - courseId value used as condition to query the database
     * @return A List of all Enrollments in the database with the specified courseId
     */
    @Override
    public List<Enrollment> getEnrollmentsByCourseId(Integer theCourseId){
        return enrollmentRepository.findByCourseId(theCourseId);
    }

    /**
     * Service method that will find all records in the database with the specified studentId and payment status
     * @param theStudentId - studentId value being used as a condition to query the database
     * @param thePaymentStatus - payment status value used as condition to query the database
     * @return A List of all Enrollments in the database with the specified studentId and payment status
     */
    @Override
    public List<Enrollment> getEnrollmentsByStudentIdAndPaymentStatus(Integer theStudentId, PayStatus thePaymentStatus) {
        return enrollmentRepository.findByStudentIdAndPaymentStatus(theStudentId, thePaymentStatus);
    }

    /**
     * Service method that will find all records in the database with the specified payment status
     * @param thePaymentStatus - payment status value used as condition to query the database
     * @return A List of all Enrollments in the database with the specified payment status
     */
    @Override
    public List<Enrollment> getEnrollmentsByPaymentStatus(PayStatus thePaymentStatus) {
        return enrollmentRepository.findByPaymentStatus(thePaymentStatus);
    }

    /**
     * Service Layer method that searches for the record with the passed enrollmentId and updates the pay status field from that record
     * @param theEnrollmentId - primary key value to update a single row in table
     * @param thePaymentStatus - value to be updated must be string type and value must be 'pending', 'cancelled', or 'completed'
     * @return returns the updated record from the table
     * @throws BadRequestException if there are no rows updated
     */
    @Override
    public Enrollment updateEnrollmentById(Integer theEnrollmentId, PayStatus thePaymentStatus) {

        int rowsUpdated = enrollmentRepository.updateEnrollmentPaymentStatusById(theEnrollmentId, thePaymentStatus);

        if (rowsUpdated == 1)
            return this.getEnrollmentById(theEnrollmentId);
        else
            throw new BadRequestException("Could not update Payment Status");
    }

    /**
     * updates an existing item
     * 
     * @param theEnrollmentId - the id of the enrollment we want to update
     * @param theCourseReview - the review of the course for that particular
     *                        enrollment
     * @return Enrollment - the updated enrollment object
     * @throws IllegalArgumentException - if the provided parameters are invalid
     * @throws RuntimeException         - if the update fails
     */
    @Override
    public Enrollment updateEnrollmentById(Integer theEnrollmentId, String theCourseReview) {
        // parameter validation
        if (theEnrollmentId == null || theCourseReview == null || theCourseReview.trim().isEmpty()) {
            throw new IllegalArgumentException("Invalid input values");
        }

        // check if the enrollment already exists in the database
        Optional<Enrollment> dBEnrollmentOptional = enrollmentRepository.findById(theEnrollmentId);

        if (!dBEnrollmentOptional.isPresent()) {
            throw new NotFoundException("Cannot update. The requested enrollment does not exist: " + theEnrollmentId);
        }

        try {
            // else get the enrollment
            Enrollment dBEnrollment = dBEnrollmentOptional.get();

            // set the enrollment course review and update
            dBEnrollment.setCourseReview(theCourseReview);

            return enrollmentRepository.save(dBEnrollment);

        } catch (Exception e) {
            // throw new exception with original as cause
            throw new RuntimeException(
                    String.format("Cannot update. Please check inputted values.\nEnrollment ID: %s\nCourse Review: %s",
                            theEnrollmentId, theCourseReview),
                    e);
        }
    }

    @Override
    public Integer deleteEnrollment(Integer theEnrollmentId) {
        try {
            enrollmentRepository.deleteById(theEnrollmentId);
            return 1;
        } catch (Exception e) {
            System.err.println("Exception occurred while deleting enrollment: " + e.getMessage());
            return 0;
        }
    }
}