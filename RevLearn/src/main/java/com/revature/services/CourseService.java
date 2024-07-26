package com.revature.services;

import java.util.List;


import com.revature.models.Course;


public interface CourseService {
    // Jason
    List<Course> getAllCourses();
    // Jason
    Course addCourse();
    // Fidel
    Course getCourseById(Integer theCourseId);
    // Chase  
    List<Course> getCoursesByEducatorId(Integer theEducatorId);
    // Alex
    Course updateCourseById(Integer theCourseId, Course theCourse);
    // Fidel
    Integer deleteCourseById(Integer theCourseId);  // admin specific function
}