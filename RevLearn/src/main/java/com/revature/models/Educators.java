package com.revature.models;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "educators", schema = "project2")
@Data
public class Educators {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private int educator_id;
    private String degree_level;
    private String degree_major;
    private String alma_mater;
    private String year;
}
