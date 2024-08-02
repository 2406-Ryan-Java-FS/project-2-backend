package com.revature.services;

import java.util.List;
import java.util.Optional;

import com.revature.exceptions.BadRequestException;
import com.revature.models.Review;
import com.revature.models.User;

import org.springframework.beans.factory.annotation.Autowired;

import com.revature.exceptions.NotFoundException;
import com.revature.exceptions.UnauthorizedException;
import com.revature.models.Enrollment;
import com.revature.models.enums.PayStatus;
import com.revature.repositories.EnrollmentRepository;
import org.springframework.stereotype.Service;

@Service
public class EnrollmentServiceImpl implements EnrollmentService {

    EnrollmentRepository enrollmentRepository;

    @Autowired
    KafkaProducerService kafkaProducerService;

    @Autowired
    public EnrollmentServiceImpl(EnrollmentRepository enrollmentRepository) {
        this.enrollmentRepository = enrollmentRepository;
    }

    /**
     * Retrieves all enrollments from the repository.
     * 
     * @return List<Enrollment> - a list of all Enrollment entities.
     * @throws RuntimeException - if the retrieval operation fails.
     */
    @Override
    public List<Enrollment> getAllEnrollments() {
        try {
            kafkaProducerService.sendRequestMessage("Getting all enrollments");
            List<Enrollment> allEnrollments = enrollmentRepository.findAll();
            return allEnrollments;
        } catch (Exception e) {
            kafkaProducerService.sendResponseMessage("Request failed: " + e.getMessage());
            throw new RuntimeException("Error fetching enrollments: " + e.getMessage());
        }
    }

    /**
     * Registers a new enrollment and returns the newly created enrollment object.
     * 
     * @param newEnrollment - the enrollment object to be created
     * @param user          - the user making the request, used for authorization
     * @return the newly created enrollment object
     * @throws UnauthorizedException if the user is not authorized to create the
     *                               enrollment
     * @throws BadRequestException   if the payment status is invalid
     */
    @Override
    public Enrollment registerEnrollment(Enrollment newEnrollment, User user) {
        Integer userId = user.getUserId();
        if (!userId.equals(newEnrollment.getStudentId())) {
            kafkaProducerService.sendResponseMessage("Request Failed. User not authorized");
            throw new UnauthorizedException("Invalid Authorization!");
        }
        if (newEnrollment.getPaymentStatus() == null) {
            kafkaProducerService.sendRequestMessage("Payment status is null");
            throw new BadRequestException(
                    "Please enter either 'pending', 'completed', or 'cancelled' for payment status.");
        }

        kafkaProducerService.sendRequestMessage("Registering new enrollment");
        return enrollmentRepository.save(newEnrollment);
    }

    /**
     * Finds a record in the Enrollments table with the specified enrollmentId.
     * 
     * @param theEnrollmentId - the ID of the enrollment to find
     * @param user            - the user requesting the enrollment information, used
     *                        for authorization
     * @return an Enrollment object if the record exists and the user is authorized
     *         to access it
     * @throws NotFoundException     if the enrollment record could not be found
     * @throws UnauthorizedException if the user is not authorized to access the
     *                               enrollment
     */
    @Override
    public Enrollment getEnrollmentById(Integer theEnrollmentId, User user) {
        Integer userId = user.getUserId();
        Enrollment enrollment = enrollmentRepository.findById(theEnrollmentId)
                .orElseThrow(() -> { 
                  kafkaProducerService.sendResponseMessage("Enrollment could not be found");
                throw new NotFoundException("Enrollment Record with ID: " + theEnrollmentId + " could not be found");
                });
                
        if (!userId.equals(enrollment.getStudentId())) {
            kafkaProducerService.sendResponseMessage("Request Failed. User not authorized");
            throw new UnauthorizedException("Invalid Authorization!");
        }
        kafkaProducerService.sendResponseMessage("Enrollment found");
        return enrollment;
    }

    /**
     * Finds an enrollment record in the database based on the specified student ID
     * and course ID.
     *
     * @param theStudentId the student ID used as a condition to query the database
     * @param theCourseId  the course ID used as a condition to query the database
     * @param user         the user requesting the enrollments, used for
     *                     authorization
     * @return an Enrollment in the database with the specified student ID and
     *         course ID
     * @throws UnauthorizedException if the user is not authorized to access the
     *                               enrollments
     * @throws NotFoundException     if no enrollment is found
     */
    @Override
    public Enrollment getEnrollmentByStudentIdAndCourseId(Integer theStudentId, Integer theCourseId, User user) {
        Integer userId = user.getUserId();
        if (!userId.equals(theStudentId)) {
            kafkaProducerService.sendResponseMessage("Request Failed. User not authorized");
            throw new UnauthorizedException("Invalid Authorization!");
        }

        Optional<Enrollment> optionalEnrollment = enrollmentRepository.findByStudentIdAndCourseId(theStudentId,
                theCourseId);

        if (optionalEnrollment.isPresent()){
            kafkaProducerService.sendRequestMessage(optionalEnrollment.get().toString());
            return optionalEnrollment.get();
        } else {
            kafkaProducerService.sendRequestMessage("Enrollment Record with Student ID: " + theStudentId + " and Course ID: " + theCourseId + " could not be found");  
            throw new NotFoundException("Enrollment Record with Student ID: " + theStudentId + " and Course ID: "
                    + theCourseId + " could not be found");
        }
    }

    @Override
    public List<Enrollment> getEnrollmentByStudentId(Integer theStudentId, User user) {
        Integer userId = user.getUserId();

        if (!userId.equals(theStudentId)) {
            kafkaProducerService.sendResponseMessage("Request Failed. User not authorized");
            throw new UnauthorizedException("Invalid Authorization!");
        }
        kafkaProducerService.sendRequestMessage("Getting all enrollments for student with ID: " + theStudentId);
        return enrollmentRepository.findByStudentId(theStudentId);
    }

    /**
     * Finds all records in the database with the specified courseId.
     * 
     * @param theCourseId - courseId value used as a condition to query the database
     * @return A List of all Enrollments in the database with the specified courseId
     */
    @Override
    public List<Enrollment> getEnrollmentsByCourseId(Integer theCourseId) {
        kafkaProducerService.sendRequestMessage("Getting all enrollments for course with ID: " + theCourseId);
        return enrollmentRepository.findByCourseId(theCourseId);
    }

    /**
     * Finds all records in the database with the specified studentId and payment
     * status.
     * 
     * @param theStudentId     - studentId value used as a condition to query the
     *                         database
     * @param thePaymentStatus - payment status value used as a condition to query
     *                         the database
     * @return A List of all Enrollments in the database with the specified
     *         studentId and payment status
     */
    @Override
    public List<Enrollment> getEnrollmentsByStudentIdAndPaymentStatus(Integer theStudentId,
            PayStatus thePaymentStatus) {
                kafkaProducerService.sendRequestMessage("Getting all enrollments for student with ID: " + theStudentId + " and payment status: " + thePaymentStatus);
        return enrollmentRepository.findByStudentIdAndPaymentStatus(theStudentId, thePaymentStatus);
    }

    /**
     * Finds all records in the database with the specified payment status.
     * 
     * @param thePaymentStatus - payment status value used as a condition to query
     *                         the database
     * @return A List of all Enrollments in the database with the specified payment
     *         status
     */
    @Override
    public List<Enrollment> getEnrollmentsByPaymentStatus(PayStatus thePaymentStatus) {
        kafkaProducerService.sendRequestMessage("Getting all enrollments with payment status: " + thePaymentStatus);
        return enrollmentRepository.findByPaymentStatus(thePaymentStatus);
    }

    /**
     * Updates the payment status field of a specific enrollment record.
     * 
     * @param theEnrollmentId  - primary key value to update a single row in the
     *                         table
     * @param thePaymentStatus - value to be updated; must be 'PENDING',
     *                         'CANCELLED', or 'COMPLETED'
     * @param user             - the user requesting the update, used for
     *                         authorization
     * @return the updated Enrollment record from the table
     * @throws NotFoundException     if the enrollment record does not exist
     * @throws UnauthorizedException if the user is not authorized to update the
     *                               enrollment
     * @throws BadRequestException   if the update fails
     */
    @Override
    public Enrollment updateEnrollmentById(Integer theEnrollmentId, PayStatus thePaymentStatus, User user) {
        Integer userId = user.getUserId();

        Enrollment enrollment = enrollmentRepository.findById(theEnrollmentId)
                .orElseThrow(() -> {
                  kafkaProducerService.sendRequestMessage("Could not update Payment Status, enrollment does not exist");
                throw  new NotFoundException("Cannot update. The requested enrollment does not exist: " + theEnrollmentId);
                });
                                        
   
        if (!userId.equals(enrollment.getStudentId())) {
            kafkaProducerService.sendResponseMessage("Request Failed. User not authorized");
            throw new UnauthorizedException("Invalid Authorization!");
        }

        int rowsUpdated = enrollmentRepository.updateEnrollmentPaymentStatusById(theEnrollmentId, thePaymentStatus);
        if (rowsUpdated != 1) {
            kafkaProducerService.sendRequestMessage("Could not update Payment Status");
            throw new BadRequestException("Could not update Payment Status");
        }
        
        kafkaProducerService.sendRequestMessage("Payment Status updated to: " + thePaymentStatus + " for Enrollment with ID: " + theEnrollmentId);
        return this.getEnrollmentById(theEnrollmentId, user);
    }

    /**
     * Updates the review and rating of a specific enrollment record.
     * 
     * @param theEnrollmentId - the ID of the enrollment we want to update
     * @param theReview       - the review of the course for that particular
     *                        enrollment
     * @param user            - the user object of the current data we want to
     *                        update
     * @return Enrollment - the updated enrollment object
     * @throws IllegalArgumentException - if the provided parameters are invalid
     * @throws RuntimeException         - if the update fails
     */
    @Override
    public Enrollment updateEnrollmentById(Integer theEnrollmentId, Review theReview, User user) {
        Integer userId = user.getUserId();

        Enrollment enrollment = enrollmentRepository.findById(theEnrollmentId)
                .orElseThrow(() -> {
                              kafkaProducerService.sendRequestMessage("Cannot update. The requested enrollment does not exist:");
                             throw new NotFoundException("Cannot update. The requested enrollment does not exist: " + theEnrollmentId);
                });
                                                   
                  
        if (!userId.equals(enrollment.getStudentId())) {
            kafkaProducerService.sendResponseMessage("Request Failed. User not authorized");
            throw new UnauthorizedException("Invalid Authorization!");
        }

        try {
            if ((theReview.getCourseReview() != null) && theReview.getCourseRating() != null) {
                if (!theReview.getCourseReview().isBlank()
                        && (theReview.getCourseRating() > 0 && theReview.getCourseRating() < 6)) {
                    enrollment.setCourseRating(theReview.getCourseRating());
                    enrollment.setCourseReview(theReview.getCourseReview());
                } else {
                    kafkaProducerService.sendRequestMessage("Invalid operation");
                    throw new BadRequestException("Invalid operation");
                }

            } else if (theReview.getCourseReview() == null && theReview.getCourseRating() != null) {
                if (theReview.getCourseRating() > 0 && theReview.getCourseRating() < 6) {
                    enrollment.setCourseRating(theReview.getCourseRating());
                } else {
                  kafkaProducerService.sendRequestMessage("Invalid operation");
                    throw new BadRequestException("Invalid operation");
                }
            } else if (theReview.getCourseReview() != null && theReview.getCourseRating() == null) {
                if (!theReview.getCourseReview().isBlank()) {
                    enrollment.setCourseReview(theReview.getCourseReview());
                } else {
                    kafkaProducerService.sendRequestMessage("Invalid operation");
                    throw new BadRequestException("Invalid operation");
                }
            }

            //throws BadRequestException if both courseRating and courseReview are null
            else {
                kafkaProducerService.sendRequestMessage("Invalid operation");
                throw new BadRequestException("Invalid operation");
            }
            kafkaProducerService.sendRequestMessage("Enrollment updated: " + enrollment.toString());

            return enrollmentRepository.save(enrollment);

        } catch (Exception e) {
            // throw new exception with original as cause
            kafkaProducerService.sendRequestMessage("Cannot update. Please check inputted values.");
          
            throw new RuntimeException(String.format(
                    "Cannot update. Please check inputted values.\nEnrollment ID: %s\nCourse Rating: %s\nCourse Review: %s",
                    theEnrollmentId, theReview.getCourseRating(), theReview.getCourseReview()), e);
        }
    }

    /**
     * Deletes an enrollment from the repository.
     * 
     * @param theEnrollmentId - the ID of the enrollment we want to delete
     * @param user            - the user making the request, used for authorization
     * @return 1 upon successful deletion or 0 if nothing was deleted
     * @throws UnauthorizedException if the user is not authorized to delete the
     *                               enrollment
     * @throws NotFoundException     if the enrollment to be deleted does not exist
     */
    @Override
    public Integer deleteEnrollment(Integer theEnrollmentId, User user) {
        Integer userId = user.getUserId();

        Enrollment enrollment = enrollmentRepository.findById(theEnrollmentId)
                .orElseThrow(() -> {
                  kafkaProducerService.sendRequestMessage("Cannot delete. The requested enrollment does not exist:");
                  throw new NotFoundException("Enrollment Record with ID: " + theEnrollmentId + " could not be found");
                });
                                      

        if (!userId.equals(enrollment.getStudentId())) {
            kafkaProducerService.sendResponseMessage("Request Failed. User not authorized");
            throw new UnauthorizedException("Invalid Authorization!");
        }

        try {
            enrollmentRepository.deleteById(theEnrollmentId);
            kafkaProducerService.sendRequestMessage("Enrollment deleted: " + theEnrollmentId);
            return 1;
        } catch (Exception e) {
            kafkaProducerService.sendRequestMessage("Exception occurred while deleting enrollment: " + e.getMessage());
            System.err.println("Exception occurred while deleting enrollment: " + e.getMessage());
            return 0;
        }
    }

}
