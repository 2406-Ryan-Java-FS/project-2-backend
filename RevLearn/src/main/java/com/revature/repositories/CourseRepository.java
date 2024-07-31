package com.revature.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.revature.models.Course;
import com.revature.models.dtos.CourseEducatorDTO;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Integer>{

    //returns a list of all records in database with the matching educatorId
    List<Course> findByEducatorId(int theEducatorId);

    @Query("SELECT new com.revature.models.dtos.CourseEducatorDTO(u.firstName, u.lastName, c)" + 
       " FROM Course c JOIN User u ON c.educatorId = u.userId WHERE c.courseId = :theCourseId")
       CourseEducatorDTO findCourseAndEducatorDetail(@Param("theCourseId") Integer theCourseId);

    @Query("SELECT new com.revature.models.dtos.CourseEducatorDTO(u.firstName, u.lastName, c) FROM Course c JOIN User u ON c.educatorId = u.userId")
    List<CourseEducatorDTO> findAllCoursesAndEducatorDetails();
}