package com.revature.controllers;

import com.revature.models.Course;
import com.revature.services.CourseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/courses")
public class CourseController {

    CourseServiceImpl CourseServiceImpl;

    @Autowired
    public CourseController(CourseServiceImpl courseServiceImpl) {
        CourseServiceImpl = courseServiceImpl;
    }




    @PutMapping("/{id}")
    public ResponseEntity<Course> updateCourseById(
            @PathVariable("id") Integer theCourseId,
            @RequestBody Course theCourse) {
        try {
            Course updatedCourse = CourseServiceImpl.updateCourseById(theCourseId, theCourse);
            if (updatedCourse != null) {
                return ResponseEntity.ok(updatedCourse);
            }
            return ResponseEntity.notFound().build();
        } catch (IllegalArgumentException e) {
            // Handle invalid educator ID error
            return ResponseEntity.badRequest().body(null);
        } catch (Exception e) {
            System.err.println("Exception occurred while updating course: " + e.getMessage());
            return ResponseEntity.status(500).build();
        }
    }
}
