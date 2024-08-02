package com.revature.services;

import java.util.List;

import com.revature.models.Course;
import com.revature.models.User;
import com.revature.models.dtos.CourseEducatorDTO;

public interface CourseService {
    List<Course> getAllCourses();

    Course addCourse(Course newCourse, User user);

    Course getCourseById(Integer theCourseId);

    List<Course> getCoursesByEducatorId(Integer theEducatorId);

    Course updateCourseById(Integer theCourseId, Course theCourse, User user);

    List<CourseEducatorDTO> getAllCoursesAndEducatorDetails();

    CourseEducatorDTO getCourseAndEducatorDetail(Integer theCourseId);

    Integer deleteCourseById(Integer theCourseId, User user); // educator specific function
}