package com.revature.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "Educator", schema = "project2")
@Data
//@JsonPropertyOrder({"educatorId", "degreeLevel", "degreeMajor", "almaMater", "year"})
public class Educator {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "educator_id")
    private int educatorId;

    @Column(name = "degree_level")
    @JsonProperty(value = "degreeLevel")
    private String degreeLevel;

    @Column(name = "degree_major")
    @JsonProperty(value = "degreeMajor")
    private String degreeMajor;

    @Column(name = "alma_mater")
    @JsonProperty(value = "almaMater")
    private String almaMater;

    @Column(name = "year")
    @JsonProperty(value = "year")
    private String year;
}
