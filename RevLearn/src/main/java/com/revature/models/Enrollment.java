package com.revature.models;

import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.revature.models.enums.PayStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;


@Entity
@Table (name = "Enrollments", schema = "project2")
@JsonPropertyOrder({"enrollmentId", "studentId", "courseId", "enrollmentDate", "paymentStatus", "enrolled", "courseRating", "courseReview"})
@Data
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @ToString
public class Enrollment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "enrollment_id", updatable = false)
    @JsonProperty(value = "enrollmentId")
    private Integer enrollmentId;

    @Column(name = "student_id")
    @JsonProperty(value = "studentId")
    private Integer studentId;

    @Column(name = "course_id ")
    @JsonProperty(value = "courseId")
    private Integer courseId;

    @Column(name = "enrollment_date")
    @JsonProperty(value = "enrollmentDate")
    private Timestamp enrollmentDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status")
    @JsonProperty(value = "paymentStatus")
    private PayStatus paymentStatus;

    @Column(name = "enrollment_status")
    @JsonProperty(value = "enrolled")
    private Boolean enrolled;

    @Column(name = "course_rating", nullable = true)
    @JsonProperty(value = "courseRating")
    private Integer courseRating;

    @Column(name = "course_review")
    @JsonProperty(value = "courseReview")
    private String courseReview;

    public Enrollment(Integer enrollmentId, Integer studentId, Integer courseId, Timestamp enrollmentDate,
            PayStatus paymentStatus, String courseReview) {
        this.enrollmentId = enrollmentId;
        this.studentId = studentId;
        this.courseId = courseId;
        this.enrollmentDate = enrollmentDate;
        this.paymentStatus = paymentStatus;
        this.courseReview = courseReview;
    }
}