package com.revature.models;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

public class Educators {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private int id;
    private String degreeLevel;
    private String degreeMajor;
    private String almaMater;
    private String year;
}
