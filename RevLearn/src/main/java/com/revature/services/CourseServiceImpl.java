package com.revature.services;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.revature.exceptions.BadRequestException;
import com.revature.exceptions.NotFoundException;
import com.revature.exceptions.UnauthorizedException;
import com.revature.models.Course;
import com.revature.models.User;
import com.revature.models.dtos.CourseEducatorDTO;
import com.revature.repositories.CourseRepository;

@Service
public class CourseServiceImpl implements CourseService {
    CourseRepository courseRepository;

    @Autowired
    public CourseServiceImpl(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    /**
     * Retrieves all courses from the repository.
     * 
     * @return List<Course> - a list of all courses
     */
    @Override
    public List<Course> getAllCourses() {
        List<Course> courses = courseRepository.findAll();
        return courses;
    }

    /**
     * Adds a new course object to the repository.
     * 
     * @param newCourse - the course object that we want to add to the repository
     * @param user      - the user making the request, used for authorization
     * @return Course - the course object if the addition was successful
     * @throws UnauthorizedException if the user is not authorized to add a course
     *                               for another educator
     * @throws BadRequestException   if the course title is null
     */
    @Override
    public Course addCourse(Course newCourse, User user) {
        Integer userId = user.getUserId();
        if (!userId.equals(newCourse.getEducatorId())) {
            throw new UnauthorizedException("You are not authorized to add a course for another educator.");
        }
        newCourse.setCreationDate(Timestamp.from(Instant.now()));

        if(newCourse.getImgUrl() == null){
            newCourse.setImgUrl(Course.IMG_URL);
        }

        if (newCourse.getTitle() == null || newCourse.getTitle().isBlank()) {
            throw new BadRequestException("Please give the new course a title.");
        }
        return courseRepository.save(newCourse);
    }

    /**
     * Retrieves a course entity from the repository.
     * 
     * @param theCourseId - the ID of the course we want to find
     * @return Course - a course object if it exists in the database
     * @throws NotFoundException - if the course does not exist
     */
    @Override
    public Course getCourseById(Integer theCourseId) {
        Optional<Course> dBCourse = courseRepository.findById(theCourseId);
        return dBCourse.orElseThrow(() -> new NotFoundException("Course does not exist. Please check the course ID: " + theCourseId));
    }

    /**
     * Service layer method that takes an educator ID and returns a list of all
     * records from the Courses table with the educatorId.
     * 
     * @param theEducatorId - ID that is being used to query the database Courses
     *                      table to find all records with the same educator_id
     *                      value
     * @return List<Course> - a list of courses that have the passed theEducatorId
     */
    @Override
    public List<Course> getCoursesByEducatorId(Integer theEducatorId) {
        return courseRepository.findByEducatorId(theEducatorId);
    }

    /**
     * Updates a course by its ID.
     * 
     * @param theCourseId - the ID of the course that we want to update
     * @param theCourse   - the course object that we want to update
     * @param user        - the user making the request, used for authorization
     * @return Course - a course object if the update was successful
     * @throws UnauthorizedException if the user is not authorized to update the
     *                               course
     * @throws NotFoundException     if the course does not exist
     */
    @Override
    public Course updateCourseById(Integer theCourseId, Course theCourse, User user) {
        Integer userId = user.getUserId();
        Course existingCourse = courseRepository.findById(theCourseId)
                .orElseThrow(() -> new NotFoundException("Course with ID: " + theCourseId + " not found"));

        if (!userId.equals(existingCourse.getEducatorId())) {
            throw new UnauthorizedException("You are not authorized to update this course.");
        }

        // Update only non-null fields, while ensuring courseId and educatorId are not changed
        if (theCourse.getTitle() != null) {
            existingCourse.setTitle(theCourse.getTitle());
        }
        if (theCourse.getDescription() != null) {
            existingCourse.setDescription(theCourse.getDescription());
        }
        if (theCourse.getCategory() != null) {
            existingCourse.setCategory(theCourse.getCategory());
        }
        if (theCourse.getPrice() != null) {
            existingCourse.setPrice(theCourse.getPrice());
        }
        if (theCourse.getImgUrl() != null) {
            existingCourse.setImgUrl(theCourse.getImgUrl());
        }
        if (theCourse.getCreationDate() != null) {
            existingCourse.setCreationDate(theCourse.getCreationDate());
        }

        return courseRepository.save(existingCourse);
    }

    /**
     * Gets all courses and educators of those courses.
     * 
     * @return List<CourseEducatorDTO> - a list of CourseEducatorDTO objects with
     *         the course and educator information
     */
    @Override
    public List<CourseEducatorDTO> getAllCoursesAndEducatorDetails() {
        try {
            List<CourseEducatorDTO> allCourseEducatorDTOs = courseRepository.findAllCoursesAndEducatorDetails();
            return allCourseEducatorDTOs;
        } catch (Exception e) {
            throw new RuntimeException("Error fetching courses and educators: " + e.getMessage());
        }
    }

    /**
     * Gets a course and the educator of that course.
     * 
     * @param theCourseId - the ID of the course that we want to fetch
     * @return CourseEducatorDTO - a CourseEducatorDTO object with the course and
     *         educator information
     * @throws NotFoundException - if the course or educator details do not exist
     */
    @Override
    public CourseEducatorDTO getCourseAndEducatorDetail(Integer theCourseId) {
        // check if the course exists
        Optional<Course> dBCourse = courseRepository.findById(theCourseId);

        if (dBCourse.isPresent()) {
            CourseEducatorDTO courseEducatorDTO = courseRepository.findCourseAndEducatorDetail(theCourseId);
            if (courseEducatorDTO.getFirstName() != null || courseEducatorDTO.getLastName() != null) {
                return courseEducatorDTO;
            } else {
                throw new NotFoundException("No educator details found for the course ID: " + theCourseId);
            }
        } else {
            throw new NotFoundException("Course does not exist. Please check the course ID: " + theCourseId);
        }
    }

    /**
     * Deletes a course from the repository.
     * 
     * @param theCourseId - the ID of the course to delete
     * @param user        - the user making the request, used for authorization
     * @return 1 upon successful deletion, or 0 if the course does not exist
     * @throws UnauthorizedException if the user is not authorized to delete the
     *                               course
     * @throws NotFoundException     if the course to be deleted does not exist
     */
    @Override
    public Integer deleteCourseById(Integer theCourseId, User user) {
        Integer userId = user.getUserId();
        // Retrieve the course by ID
        Course course = courseRepository.findById(theCourseId)
                .orElseThrow(() -> new NotFoundException("Course with ID: " + theCourseId + " not found"));

        // Check if the user is authorized to delete the course
        if (!userId.equals(course.getEducatorId())) {
            throw new UnauthorizedException("You are not authorized to delete this course");
        }

        // Delete the course
        courseRepository.deleteById(theCourseId);
        return 1;
    }
}
