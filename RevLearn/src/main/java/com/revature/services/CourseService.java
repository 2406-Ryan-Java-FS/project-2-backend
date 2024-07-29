package com.revature.services;

import java.util.List;

import com.revature.models.Course;

public interface CourseService {
    List<Course> getAllCourses();

    Course addCourse(Course newCourse);

    Course getCourseById(Integer theCourseId);

    List<Course> getCoursesByEducatorId(Integer theEducatorId);

    Course updateCourseById(Integer theCourseId, Course theCourse);

    Integer deleteCourseById(Integer theCourseId); // admin specific function
}