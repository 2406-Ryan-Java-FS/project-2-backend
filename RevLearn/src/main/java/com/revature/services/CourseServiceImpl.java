package com.revature.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import com.revature.exceptions.BadRequestException;
import com.revature.exceptions.NotFoundException;
import com.revature.models.Course;
import com.revature.repositories.CourseRepository;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Service;

@Service
public class CourseServiceImpl implements CourseService {
    CourseRepository courseRepository;

    @Autowired
    public CourseServiceImpl(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    /**
     *  This method returns a list of all courses
     *  
     * @return List
     */
    @Override
    public List<Course> getAllCourses() {
        List<Course> courses = courseRepository.findAll();
        return courses;
    }

    /**
     *  This method takes in a new course oject 
     * returns the new course object
     * 
     * @param 
     * @return object
     */
    @Override
    public Course addCourse(Course newCourse) {
        
        if(newCourse.getTitle() == null){
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
     * This method takes in an int to query the Courses in the database and return a
     * list of all courses with the matching
     * id
     * 
     * @param theEducatorId
     * @return List
     */
    @Override
    public List<Course> getCoursesByEducatorId(Integer theEducatorId) {
        return courseRepository.findByEducatorId(theEducatorId);
    }

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
