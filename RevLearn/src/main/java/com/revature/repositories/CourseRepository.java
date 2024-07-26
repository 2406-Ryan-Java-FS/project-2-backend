package com.revature.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.revature.models.Course;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Integer>{

    //returns a list of all records in database with the matching educatorId
    List<Course> findByEducatorId(int theEducatorId);
}