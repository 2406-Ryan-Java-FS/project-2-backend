package com.revature.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.revature.exceptions.NotFoundException;
import com.revature.models.Course;
import com.revature.repositories.CourseRepository;

@Service
public class CourseServiceImpl implements CourseService{
    CourseRepository courseRepository;

    @Autowired
    public CourseServiceImpl(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @Override
    public List<Course> getAllCourses() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAllCourses'");
    }

    @Override
    public Course addCourse() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'addCourse'");
    }

    /**
     * retrieves a course entitiy from the repository
     * @param theCourseId - the id of the course we want to find
     * @return Course - a course object if it exists in the database
     * @throws NotFoundException - if the course does not exist
     */
    @Override
    public Course getCourseById(Integer theCourseId) {

        Optional<Course> dBCourse = courseRepository.findById(theCourseId);

        if (dBCourse.isPresent()) 
        {
            return dBCourse.get();
        } 
        else 
        {
            throw new NotFoundException("Course does not exist. Please check the course ID: " + theCourseId);
        }
    }

    /**
     * service layer method that takes an educator id and returns a list of all records from the Courses table with the educatorId
     * @param theEducatorId - id that is being used to query the database Courses table to find all records with the same educator_id value
     * @return returns a List of Courses that have the passed theEducatorId
     */
    @Override
    public List<Course> getCoursesByEducatorId(Integer theEducatorId) {
        return courseRepository.findByEducatorId(theEducatorId);
    }

    @Override
    public Course updateCourseById(Integer theCourseId, Course theCourse) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateCourseById'");
    }

    /**
     * deletes a trainer from the repository
     * @param theCourseId - the id of the course data that we want to delete
     * @return 1 or 0 - the number of affected table rows
     */
    @Override
    public Integer deleteCourseById(Integer theCourseId) {
        // if the account doesn't exist
        if(courseRepository.findById(theCourseId).isPresent())
        {
            courseRepository.deleteById(theCourseId);
            return 1;
        }
        else
        {
            return 0;
        }
    }
}
