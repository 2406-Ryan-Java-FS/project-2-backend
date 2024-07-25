package com.revature.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Table(name = "users", schema = "project2")
@Data
@JsonPropertyOrder({"userId","firstName","lastName","email","password","role","registrationDate"})
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    @JsonProperty(value = "userId")
    private  int userId;

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
    private String role;

    @Column(name = "registration_date")
    @JsonProperty(value = "registrationDate")
    private Date registrationDate;

    @Column(name = "professional_details")
    @JsonProperty(value = "professionalDetails")
    private String professionalDetails;

    public int getUserId() {
    return userId;
}
public void setUserId(int userId) {
    this.userId = userId;
}
public String getFirstName() {
    return firstName;
}
public void setFirstName(String firstName) {
    this.firstName = firstName;
}
public String getLastName() {
    return lastName;
}
public void setLastName(String lastName) {
    this.lastName = lastName;
}

public String getEmail() {
    return email;
}

public void setEmail(String email) {
    this.email = email;
}

public String getPassword() {
    return password;
}

public void setPassword(String password) {
    this.password = password;
}

public String getRole() {
    return role;
}

public void setRole(String role) {
    this.role = role;
}

public Date getResgistrationDate() {
    return registrationDate;
}

public void setRegistratinDate(Date registrationDate) {
    this.registrationDate = registrationDate;
}

public String getProfessionalDetails() {
    return professionalDetails;
}

public void setProfessionalDetails(String professionalDetails) {
    this.professionalDetails = professionalDetails;
}
}


