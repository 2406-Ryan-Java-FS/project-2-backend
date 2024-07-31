package com.revature.models.dtos;

import com.revature.models.Course;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CourseEducatorDTO {
    private String firstName;
    private String lastName;
    private Course course;

    public CourseEducatorDTO(String firstName, String lastName, Course course) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.course = course;
    }
}
