package com.revature.models;

import java.sql.Timestamp;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table (name = "Courses", schema = "project2")
@JsonPropertyOrder({"courseId", "educatorId", "title", "description", "category", "price", "imgUrl", "creationDate"})
@Data
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @ToString
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "course_id", updatable = false)
    @JsonProperty(value = "courseId")
    private Integer courseId;

    @Column(name = "educator_id")
    @JsonProperty(value = "educatorId")
    private Integer educatorId;

    @Column(name = "title")
    @JsonProperty(value = "title")
    private String title;

    @Column(name = "description")
    @JsonProperty(value = "description")
    private String description;

    @Column(name = "category")
    @JsonProperty(value = "category")
    private String category;

    @Column(name = "price", columnDefinition = "NUMERIC(10, 2)")
    @JsonProperty(value = "price")
    private double price;

    @Column(name = "img_url")
    @JsonProperty(value = "imgUrl")
    private String imgUrl;

    @Column(name = "creation_date")
    @JsonProperty(value = "creationDate")
    private Timestamp creationDate;
}
