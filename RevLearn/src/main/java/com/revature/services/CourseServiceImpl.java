package com.revature.services;

import java.util.List;

import com.revature.models.Enrollment;
import org.springframework.beans.factory.annotation.Autowired;

import com.revature.exceptions.BadRequestException;
import com.revature.models.Course;
import com.revature.repositories.CourseRepository;

public class CourseServiceImpl implements CourseService{
    CourseRepository courseRepository;

    @Autowired
    public CourseServiceImpl(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @Override
    public List<Course> getAllCourses() {
        List<Course> courses = courseRepository.findAll();
        return courses;
    }

    @Override
    public Course addCourse(Course newCourse) {
        
        if(newCourse.getTitle() == null){
            throw new BadRequestException("Please give the new course a title.");
        }

        Course dbCourse = courseRepository.save(newCourse);
        return dbCourse;
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
