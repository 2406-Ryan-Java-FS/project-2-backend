package com.revature.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/*
    July 25
    Pinging this to do various things during development
    Future: may clear database tables during testing on local machine or server
 */
@Profile("h2")
@RestController
@RequestMapping("/development")
public class TestController {

    private static final Logger logger= LoggerFactory.getLogger(TestController.class);

    @GetMapping()
    public String slash(){
        logger.info("Reached this endpoint");
        return "This is an endpoint";
    }
}
