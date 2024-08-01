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
            throw new BadRequestException("Please give the new course a title.");
        }
        Course dbCourse = courseRepository.save(newCourse);
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
            return dBCourse.get();
        } else {
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
        return courseRepository.findByEducatorId(theEducatorId);
    }

    /**
     * updates a course by it's id
     * 
     * @param theCourseId - the id of the course that we want to update
     * @param theCourse   - the course object that we want to update
     * @return Course - a course object if the update was successful
     */
    @Override
    public Course updateCourseById(Integer theCourseId, Course theCourse) {
        try {
            if (!courseRepository.existsById(theCourse.getEducatorId())) {
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
                return courseRepository.save(existingCourse);
            }
            return null;
        } catch (Exception e) {
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
            return allCourseEducatorDTOs;
        } catch (Exception e) {
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
                return courseEducatorDTO;
            } else {
                throw new NotFoundException("No educator details found for the course ID: " + theCourseId);
            }
        } else {
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
            return 1;
        } else {
            return 0;
        }
    }
}
