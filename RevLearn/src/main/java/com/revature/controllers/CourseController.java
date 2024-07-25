package com.revature.controllers;

import java.util.List;

import com.revature.exceptions.BadRequestException;
import com.revature.models.Course;
import com.revature.services.CourseService;

import io.micrometer.core.ipc.http.HttpSender.Response;

// import org.hibernate.mapping.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CourseController {

    CourseService courseService;

    @Autowired
    public CourseController( CourseService courseService){
        this.courseService = courseService;
    }

    @GetMapping("/course")
    public ResponseEntity<?> getAllCourses(){
        List<Course> allCourses = courseService.getAllCourses();
        return ResponseEntity.ok(allCourses);
    }

    @PostMapping("/course")
    public ResponseEntity<?> addNewCourse(@RequestBody Course newCourse){
        try{
            Course course = courseService.addCourse(newCourse);
            return ResponseEntity.ok(course);
        }catch (BadRequestException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
