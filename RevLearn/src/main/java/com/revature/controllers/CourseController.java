package com.revature.controllers;

import com.revature.exceptions.NotFoundException;
import com.revature.models.Course;
import com.revature.services.CourseService;
import com.revature.services.CourseServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

public class CourseController {

    CourseService courseService;

    @Autowired
    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    /**
     * handler to get a course by id
     * 
     * @param theCourseId - the id of the course to retrieve
     * @return a response entity containing the course if found or a Bad Request
     *         error message if not found
     */
    @GetMapping("/courses/{theCourseId}")
    ResponseEntity<?> getCourseByIdgetCourseById(@PathVariable Integer theCourseId) {
        try {
            Course dBCourse = courseService.getCourseById(theCourseId);
            return ResponseEntity.ok(dBCourse);
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    /**
     * handler to delete course data
     * 
     * @param theCourseId - the id of the course that we want to delete in the
     *                    database
     * @return a response entity containing 1 if a course is deleted or 0 if the
     *         course is not deleted
     */
    @DeleteMapping("/courses/{theCourseId}")
    public ResponseEntity<?> deleteCourseById(@PathVariable Integer theCourseId) {
        Integer rowsAffected = courseService.deleteCourseById(theCourseId);

        if (rowsAffected == 0) {
            return ResponseEntity.ok(rowsAffected + " course deleted.");
        } else {
            return ResponseEntity.ok(rowsAffected + " course deleted.");
        }
    }

    @PutMapping("/courses/{id}")
    public ResponseEntity<Course> updateCourseById(
            @PathVariable("id") Integer theCourseId,
            @RequestBody Course theCourse) {
        try {
            Course updatedCourse = courseService.updateCourseById(theCourseId, theCourse);
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