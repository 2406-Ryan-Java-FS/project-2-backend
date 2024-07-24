package com.revature.models;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "users",schema = "project2")
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer user_id;

    private String first_name;
    private  String last_name;
    private String password;
    private String role;

}
