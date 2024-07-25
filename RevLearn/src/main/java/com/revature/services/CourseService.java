package com.revature.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.revature.models.Course;

@Service
public interface CourseService {
    // Jason
    List<Course> getAllCourses();
    // Jason
    Course addCourse(Course newCourse);
    // Fidel
    Course getCourseById(Integer theCourseId);
    // Chase  
    List<Course> getCoursesByEducatorId(Integer theEducatorId);
    // Alex
    Course updateCourseById(Integer theCourseId, Course theCourse);
    // Fidel
    Integer deleteCourse(Integer theCourseId);  // admin specific function
}