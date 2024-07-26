package com.revature.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import com.revature.models.Course;
import com.revature.repositories.CourseRepository;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Service;

@Service
public class CourseServiceImpl implements CourseService{
    CourseRepository courseRepository;

    @Autowired
    public CourseServiceImpl(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @Override
    public List<Course> getAllCourses() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAllCourses'");
    }

    @Override
    public Course addCourse() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'addCourse'");
    }

    @Override
    public Course getCourseById(Integer theCourseId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getCourseById'");
    }

    @Override
    public List<Course> getCoursesByEducatorId(Integer theEducatorId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getCoursesByEducatorId'");
    }

    @Override
    public Course updateCourseById(Integer theCourseId, Course theCourse) {
            try {
                if (!courseRepository.existsById(theCourse.getEducatorId())) {
                    throw new IllegalArgumentException("Educator ID " + theCourse.getEducatorId() + " does not exist.");
                }
                Optional<Course> optionalCourse = courseRepository.findById(theCourseId);
                if (optionalCourse.isPresent()) {
                    Course existingCourse = optionalCourse.get();
                    existingCourse.setTitle(theCourse.getTitle());
                    existingCourse.setDescription(theCourse.getDescription());
                    existingCourse.setCategory(theCourse.getCategory());
                    existingCourse.setPrice(theCourse.getPrice());
                    existingCourse.setEducatorId(theCourse.getEducatorId());
                    return courseRepository.save(existingCourse);
                }
                return null;
            } catch (Exception e) {
                System.err.println("Exception occurred while updating course: " + e.getMessage());
                return null;
            }
    }

    @Override
    public Integer deleteCourse(Integer theCourseId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteCourse'");
    } 
}
