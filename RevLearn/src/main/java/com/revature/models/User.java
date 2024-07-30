package com.revature.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.revature.models.enums.Role;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "users", schema = "project2")
@Data
@JsonPropertyOrder({"userId", "firstName", "lastName", "email", "password", "role", "registrationDate"})
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    @JsonProperty(value = "userId")
    private int userId;

    @Column(name = "first_name")
    @JsonProperty(value = "firstName")
    private String firstName;

    @Column(name = "last_name")
    @JsonProperty(value = "lastName")
    private String lastName;

    @Column(name = "email")
    @JsonProperty(value = "email")
    private String email;

    @Column(name = "password")
    @JsonProperty(value = "password")
    private String password;

    @Column(name = "role")
    @JsonProperty(value = "role")
    @Enumerated(EnumType.STRING)
    private Role role;
}
