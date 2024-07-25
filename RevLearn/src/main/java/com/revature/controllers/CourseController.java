package com.revature.controllers;


import com.revature.services.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CourseController {

    CourseService courseService;

    @Autowired
    public CourseController( CourseService courseService){
        this.courseService = courseService;
    }
}
