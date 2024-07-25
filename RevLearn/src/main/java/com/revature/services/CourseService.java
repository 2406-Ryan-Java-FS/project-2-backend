package com.revature.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.revature.models.Course;

@Service
public interface CourseService {
    List<Course> getAllCourses();
    Course addCourse();
    Course getCourseById(Integer theCourseId);  
    List<Course> getCoursesByEducatorId(Integer theEducatorId);
    Course updateCourseById(Integer theCourseId, Course theCourse);
    Integer deleteCourse(Integer theCourseId);  // admin specific function
}