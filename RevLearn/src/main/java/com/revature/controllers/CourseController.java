package com.revature.controllers;

import java.util.List;
import com.revature.exceptions.BadRequestException;
import com.revature.exceptions.NotFoundException;
import com.revature.exceptions.UnauthorizedException;
import com.revature.models.Course;
import com.revature.models.User;
import com.revature.models.dtos.CourseEducatorDTO;
import com.revature.services.CourseService;
import com.revature.services.JwtService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
public class CourseController {

    CourseService courseService;
    JwtService jwtService;

    @Autowired
    public CourseController(CourseService courseService, JwtService jwtService) {
        this.courseService = courseService;
        this.jwtService = jwtService;
    }

    // GET Requests

    /**
     * Handler to get all courses.
     * 
     * @return ResponseEntity containing a list of courses
     */
    @GetMapping("/courses")
    public ResponseEntity<List<Course>> getAllCourses() {
        List<Course> allCourses = courseService.getAllCourses();
        return ResponseEntity.ok(allCourses);
    }

    /**
     * Handler to get a course by ID.
     * 
     * @param theCourseId - the ID of the course to retrieve
     * @return ResponseEntity containing the course if found or a Bad Request error
     *         message if not found
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
     * Handler to get a list of courses and educators.
     * 
     * @return ResponseEntity wrapping a list of courses and educators or a not
     *         found exception
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
     * Handler to get a course and educator by the course ID.
     * 
     * @param theCourseId - the ID of the course and educator details we want to
     *                    retrieve
     * @return ResponseEntity containing the CourseEducatorDTO if found or a
     *         NOT_FOUND exception if not found
     */
    @GetMapping("/courses/educators/details/{theCourseId}")
    public ResponseEntity<?> getCourseAndEducatorDetail(@PathVariable Integer theCourseId) {
        try {
            CourseEducatorDTO courseEducatorDTO = courseService.getCourseAndEducatorDetail(theCourseId);
            return ResponseEntity.ok(courseEducatorDTO);
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    /**
     * Handler to get courses by a specific educator ID.
     * 
     * @param theEducatorId - the educator ID of the list of courses
     * @return ResponseEntity containing a list of courses by a specific educator
     */
    @GetMapping("/courses/educators/{theEducatorId}")
    public ResponseEntity<List<Course>> getCoursesWithEducatorId(@PathVariable Integer theEducatorId) {
        return ResponseEntity.ok(courseService.getCoursesByEducatorId(theEducatorId));
    }

    // POST Request

    /**
     * Handler to add a new course.
     * 
     * @param newCourse - course object that contains all fields
     * @param token     - the authorization token to identify the user making the
     *                  request
     * @return ResponseEntity containing the new course or a Bad Request error
     *         message if required fields are not filled out
     */
    @PostMapping("/courses")
    public ResponseEntity<?> addNewCourse(@RequestBody Course newCourse,
            @RequestHeader(name = "Authorization") String token) {
        User user = jwtService.getUserFromToken(token);
        try {
            Course course = courseService.addCourse(newCourse, user);
            return ResponseEntity.ok(course);
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        } catch (BadRequestException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // PATCH Request

    /**
     * Handler to update a course by its course ID.
     * 
     * @param theCourseId - the ID of the course that we want to update
     * @param theCourse   - the course object that we want to update
     * @param token       - the authorization token to identify the user making the
     *                    request
     * @return ResponseEntity<Course> - a course object wrapped in a Response entity
     *         upon success or an exception upon failure
     */
    @PatchMapping("/courses/{theCourseId}")
    public ResponseEntity<Course> updateCourseById(
            @PathVariable Integer theCourseId,
            @RequestBody Course theCourse, @RequestHeader(name = "Authorization") String token) {
        User user = jwtService.getUserFromToken(token);
        try {
            Course updatedCourse = courseService.updateCourseById(theCourseId, theCourse, user);
            if (updatedCourse != null) {
                return ResponseEntity.ok(updatedCourse);
            }
            return ResponseEntity.notFound().build();
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        } catch (Exception e) {
            System.err.println("Exception occurred while updating course: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // DELETE Request

    /**
     * Handler to delete course data.
     * 
     * @param theCourseId - the ID of the course to delete from the database
     * @param token       - the authorization token to identify the user making the
     *                    request
     * @return ResponseEntity containing the result of the deletion
     */
    @DeleteMapping("/courses/{theCourseId}")
    public ResponseEntity<?> deleteCourseById(@PathVariable Integer theCourseId,
            @RequestHeader(name = "Authorization") String token) {
        User user = jwtService.getUserFromToken(token);
        try {
            Integer rowsAffected = courseService.deleteCourseById(theCourseId, user);
            return ResponseEntity.ok(rowsAffected + " course deleted.");
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while deleting the course.");
        }
    }
}