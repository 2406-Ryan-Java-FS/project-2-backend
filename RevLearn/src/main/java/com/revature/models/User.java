package com.revature.models;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Table(name = "users", schema = "project2")
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  int user_id;

    private String first_name;
    private String last_name;
    private String email;
    private String password;
    private String role;
    private Date registration_date;

}
