package com.revature.controllers;

import com.revature.models.Educator;
import com.revature.services.EducatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/educators")
public class EducatorController {

    EducatorService educatorService;

    @Autowired
    public EducatorController(EducatorService educatorService) {
        this.educatorService = educatorService;
    }

    /**
     * Endpoint for registering a new Educator.
     *
     * @param educator The Educator to be registered.
     * @return The persisted Educator with its newly assigned educatorId.
     */
    @PostMapping
    public ResponseEntity<Educator> addEducator(@RequestBody Educator educator) {

        Educator newEducator = educatorService.addEducator(educator);
        return new ResponseEntity<>(newEducator, HttpStatus.CREATED);
    }

    /**
     * Endpoint for retrieving an Educator given its educatorId.
     *
     * @param educatorId The educatorId of the Educator to retrieve.
     * @return The Educator along with a 200 status code.
     */
    @GetMapping("/{userId}")
    public ResponseEntity<Educator> getEducator(@PathVariable Integer educatorId) {

        Educator existingEducator = educatorService.getEducator(educatorId);
        return new ResponseEntity<>(existingEducator, HttpStatus.OK);
    }
}
