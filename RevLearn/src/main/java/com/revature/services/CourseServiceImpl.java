package com.revature.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.revature.models.Course;
import com.revature.repositories.CourseRepository;
import org.springframework.stereotype.Service;

@Service
public class CourseServiceImpl implements CourseService{
    CourseRepository courseRepository;

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

    @Override
    public Course getCourseById(Integer theCourseId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getCourseById'");
    }


    /**
     *  This method takes in an int to query the Courses in the database and return a list of all courses with the matching
     *  id
     * @param theEducatorId
     * @return List
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

    @Override
    public Integer deleteCourse(Integer theCourseId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteCourse'");
    } 
}
