package com.revature.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.revature.models.Course;

@Repository
public interface CourseRepository extends JpaRepository<Course, Integer>{
}