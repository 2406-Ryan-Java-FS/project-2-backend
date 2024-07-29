package com.revature.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "educators", schema = "project2")
@Data
@JsonPropertyOrder({"educatorId", "degreeLevel", "degreeMajor", "almaMater", "year"})
public class Educator {

    @Id
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

    @Column(name = "year")
    @JsonProperty(value = "year")
    private String year;
}
