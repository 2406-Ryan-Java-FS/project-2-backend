package com.revature.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "educator", schema = "project2")
@Data
public class Educator {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "educator_id")
    @JsonProperty(value = "educatorId")
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

    /*
    WATCH OUT!!!! "year" is a reserved keyword so don't use it!!!
     */
    @Column(name = "year_of_hire")
    @JsonProperty(value = "yearOfHire")
    private String yearOfHire;
}
