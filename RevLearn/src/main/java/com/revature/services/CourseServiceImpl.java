package com.revature.services;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.revature.exceptions.BadRequestException;
import com.revature.exceptions.NotFoundException;
import com.revature.models.Course;
import com.revature.models.dtos.CourseEducatorDTO;
import com.revature.repositories.CourseRepository;

@Service
public class CourseServiceImpl implements CourseService {
    CourseRepository courseRepository;

    @Autowired
    KafkaProducerService kafkaProducerService;

    @Autowired
    public CourseServiceImpl(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    /**
     * retrieves all courses from the repository
     * 
     * @return List - a list of all courses
     */
    @Override
    public List<Course> getAllCourses() {
        List<Course> courses = courseRepository.findAll();

        kafkaProducerService.sendResponseMessage(courses.toString());

        return courses;
    }

    /**
     * This method adds a new course object to the repository
     * 
     * @param newCourse - the course object that we want to add to the repository
     * @return Course - the course object if the addition was successful
     */
    @Override
    public Course addCourse(Course newCourse) {

        newCourse.setCreationDate(Timestamp.from(Instant.now()));

        if(newCourse.getImgUrl() == null){
            newCourse.setImgUrl(Course.IMG_URL);
        }

        if (newCourse.getTitle() == null) {
            kafkaProducerService.sendResponseMessage("Request Failed. Course had no title");
            throw new BadRequestException("Please give the new course a title.");
        }
        Course dbCourse = courseRepository.save(newCourse);

        kafkaProducerService.sendResponseMessage(dbCourse.toString());
        return dbCourse;
    }

    /**
     * retrieves a course entitiy from the repository
     * 
     * @param theCourseId - the id of the course we want to find
     * @return Course - a course object if it exists in the database
     * @throws NotFoundException - if the course does not exist
     */
    @Override
    public Course getCourseById(Integer theCourseId) {

        Optional<Course> dBCourse = courseRepository.findById(theCourseId);

        if (dBCourse.isPresent()) {
            kafkaProducerService.sendResponseMessage(dBCourse.get().toString());
            return dBCourse.get();
        } else {
            kafkaProducerService.sendResponseMessage("Course with ID: " + theCourseId + " does not exist");
            throw new NotFoundException("Course does not exist. Please check the course ID: " + theCourseId);
        }
    }

    /**
     * service layer method that takes an educator id and returns a list of all
     * records from the Courses table with the educatorId
     * 
     * @param theEducatorId - id that is being used to query the database Courses
     *                      table to find all records with the same educator_id
     *                      value
     * @return returns a List of Courses that have the passed theEducatorId
     */
    @Override
    public List<Course> getCoursesByEducatorId(Integer theEducatorId) {

        List<Course> courses = courseRepository.findByEducatorId(theEducatorId);

        kafkaProducerService.sendResponseMessage(courses.toString());
        return courses;
    }

    /**
     * updates a course by its id
     * 
     * @param theCourseId - the id of the course that we want to update
     * @param theCourse   - the course object that we want to update
     * @return Course - a course object if the update was successful
     */
    @Override
    public Course updateCourseById(Integer theCourseId, Course theCourse) {
        try {
            if (!courseRepository.existsById(theCourse.getEducatorId())) {
                kafkaProducerService.sendResponseMessage("Educator ID " + theCourse.getEducatorId() + " does not exist.");
                throw new IllegalArgumentException("Educator ID " + theCourse.getEducatorId() + " does not exist.");
            }
            Optional<Course> optionalCourse = courseRepository.findById(theCourseId);
            if (optionalCourse.isPresent()) {
                Course existingCourse = optionalCourse.get();
                existingCourse.setTitle(theCourse.getTitle());
                existingCourse.setDescription(theCourse.getDescription());
                existingCourse.setCategory(theCourse.getCategory());
                existingCourse.setPrice(theCourse.getPrice());
                existingCourse.setEducatorId(theCourse.getEducatorId());

                kafkaProducerService.sendResponseMessage(existingCourse.toString());
                return courseRepository.save(existingCourse);
            }
            return null;
        } catch (Exception e) {
            kafkaProducerService.sendResponseMessage("Exception occurred while updating course: " + e.getMessage());
            System.err.println("Exception occurred while updating course: " + e.getMessage());
            return null;
        }
    }

    /**
     * gets all courses and educators of those courses
     * 
     * @return List<CourseEducatorDTO> - a list of CourseEducatorDTO objects with
     *         the course and
     *         educator information
     */
    @Override
    public List<CourseEducatorDTO> getAllCoursesAndEducatorDetails() {
        try {
            List<CourseEducatorDTO> allCourseEducatorDTOs = courseRepository.findAllCoursesAndEducatorDetails();
            kafkaProducerService.sendResponseMessage(allCourseEducatorDTOs.toString());
            return allCourseEducatorDTOs;
        } catch (Exception e) {
            kafkaProducerService.sendResponseMessage("Error fetching courses and educators: " + e.getMessage());
            throw new RuntimeException("Error fetching courses and educators: " + e.getMessage());
        }
    }

    /**
     * gets a course and the educator of that course
     * 
     * @param theCourseId - the id of the course that we want to fetch
     * @return CourseEducatorDTO - a CourseEducatorDTO object with the course and
     *         educator information
     */
    @Override
    public CourseEducatorDTO getCourseAndEducatorDetail(Integer theCourseId) {
        // check if the course exists
        Optional<Course> dBCourse = courseRepository.findById(theCourseId);

        if (dBCourse.isPresent()) {
            CourseEducatorDTO courseEducatorDTO = courseRepository.findCourseAndEducatorDetail(theCourseId);
            if (courseEducatorDTO.getFirstName() != null || courseEducatorDTO.getLastName() != null) {
                kafkaProducerService.sendResponseMessage(courseEducatorDTO.toString());
                return courseEducatorDTO;
            } else {
                kafkaProducerService.sendResponseMessage("No educator details found for the course ID: " + theCourseId);
                throw new NotFoundException("No educator details found for the course ID: " + theCourseId);
            }
        } else {
            kafkaProducerService.sendResponseMessage("Course does not exist. Please check the course ID: " + theCourseId);
            throw new NotFoundException("Course does not exist. Please check the course ID: " + theCourseId);
        }
    }

    /**
     * deletes a trainer from the repository
     * 
     * @param theCourseId - the id of the course data that we want to delete
     * @return 1 or 0 - the number of affected table rows
     */
    @Override
    public Integer deleteCourseById(Integer theCourseId) {
        // if the account doesn't exist
        if (courseRepository.findById(theCourseId).isPresent()) {
            courseRepository.deleteById(theCourseId);
            kafkaProducerService.sendResponseMessage("Courses Deleted: 1");
            return 1;
        } else {
            kafkaProducerService.sendResponseMessage("Courses Deleted: 0");
            return 0;
        }
    }
}
