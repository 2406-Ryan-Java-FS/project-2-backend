package com.revature.controllers;

import java.util.List;
import com.revature.exceptions.BadRequestException;
import com.revature.exceptions.NotFoundException;
import com.revature.models.Course;
import com.revature.models.dtos.CourseEducatorDTO;
import com.revature.services.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class CourseController {

    CourseService courseService;

    @Autowired
    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    /**
     * handler to get all courses
     * 
     * @return ResponseEntity containing a list of courses
     */
    @GetMapping("/courses")
    public ResponseEntity<List<Course>> getAllCourses() {
        List<Course> allCourses = courseService.getAllCourses();
        return ResponseEntity.ok(allCourses);
    }

    /**
     * handler to add a new course
     * 
     * @param newCourse - course object that contains all fields
     * @return a response entity containing the new course or a Bad Request
     *         error message if not null entitrys are not filled out
     */
    @PostMapping("/courses")
    public ResponseEntity<?> addNewCourse(@RequestBody Course newCourse) {
        try {
            Course course = courseService.addCourse(newCourse);
            return ResponseEntity.ok(course);
        } catch (BadRequestException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    /**
     * handler to get a course by id
     * 
     * @param theCourseId - the id of the course to retrieve
     * @return a response entity containing the course if found or a Bad Request
     *         error message if not found
     */
    @GetMapping("/courses/{theCourseId}")
    ResponseEntity<?> getCourseById(@PathVariable Integer theCourseId) {
        try {
            Course dBCourse = courseService.getCourseById(theCourseId);
            return ResponseEntity.ok(dBCourse);
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    /**
     * handler to get a list of courses and educators
     * @return List<CourseEducatorDTO> - a Response entity wrapping a list of courses and educators
     * or a not found exception
     */
    @GetMapping("/courses/educators/details")
    public ResponseEntity<?> getAllCoursesAndEducatorDetails() {
        try {
            List<CourseEducatorDTO> allCourseEducatorDTOs = courseService.getAllCoursesAndEducatorDetails();
            return ResponseEntity.ok(allCourseEducatorDTOs);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    /**
     * handler to get a course and educator by the course id
     * 
     * @param theCourseId - the id of the course and educator details we want to retrieve
     * @return a response entity containing the CourseEducatorDTO if found or a NOT_FOUND exception
     *         if not found
     */
    @GetMapping("/courses/educators/details/{theCourseId}")
    public ResponseEntity<?> getCourseAndEducatorDetail(@PathVariable Integer theCourseId) {
        try 
        {
            CourseEducatorDTO courseEducatorDTO = courseService.getCourseAndEducatorDetail(theCourseId);
            return ResponseEntity.ok(courseEducatorDTO);
        } 
        catch (NotFoundException e) 
        {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
    
    /**
     * handler to get courses by a specific educator id
     * 
     * @param theEducatorId - the educator id of the list of courses
     * @return a response entity containing a list of courses by a specific educator
     */
    @GetMapping("/courses/educators/{theEducatorId}")
    public ResponseEntity<List<Course>> getCoursesWithEducatorId(@PathVariable Integer theEducatorId) {
        return ResponseEntity.ok(courseService.getCoursesByEducatorId(theEducatorId));
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

    /**
     * handler to update a course by its course id
     * 
     * @param theCourseId - the id of the course that we want to update
     * @param theCourse   - the course object that we want to update
     * @return ResponseEntity<Course> - a course object wrapped in a Response entity
     *         upon success or an exception upon failure
     */
    @PutMapping("/courses/{theCourseId}")
    public ResponseEntity<Course> updateCourseById(
            @PathVariable Integer theCourseId,
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