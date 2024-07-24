package com.revature.models;

import java.sql.Date;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


enum PayStatus{
    pending,
    completed,
    cancelled,
}

@Entity
@Table (name = "Enrollments", schema = "project2")
@JsonPropertyOrder({"enrollmentId", "studentId", "courseId", "enrollmentDate", "paymentStatus", "courseReview"})
@Getter @Setter @NoArgsConstructor @ToString
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
    private Integer courseId ;

    @Column(name = "enrollment_date")
    @JsonProperty(value = "enrollmentDate")
    private Date enrollmentDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status")
    @JsonProperty(value = "paymentStatus")
    private PayStatus paymentStatus;

    @Column(name = "course_review")
    @JsonProperty(value = "courseReview")
    private String courseReview;

    public Enrollment(Integer enrollmentId, Integer studentId, Integer courseId, Date enrollmentDate,
            PayStatus paymentStatus, String courseReview) {
        this.enrollmentId = enrollmentId;
        this.studentId = studentId;
        this.courseId = courseId;
        this.enrollmentDate = enrollmentDate;
        this.paymentStatus = paymentStatus;
        this.courseReview = courseReview;
    }
}