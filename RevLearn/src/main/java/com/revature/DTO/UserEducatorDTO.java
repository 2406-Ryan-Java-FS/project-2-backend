package com.revature.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.revature.models.Educator;
import com.revature.models.User;
import com.revature.models.enums.Role;
import lombok.Data;

/**
 * Data Transfer Object (DTO) for combining User and Educator information.
 */
@Data
@JsonPropertyOrder({"userId", "firstName", "lastName", "email", "password", "role", "degreeLevel", "degreeMajor", "almaMater", "year"})
public class UserEducatorDTO {

    @JsonProperty(value = "userId")
    private int userId;

    @JsonProperty(value = "firstName")
    private String firstName;

    @JsonProperty(value = "lastName")
    private String lastName;

    @JsonProperty(value = "email")
    private String email;

    @JsonProperty(value = "password")
    private String password;

    @JsonProperty(value = "role")
    private Role role;

    @JsonProperty(value = "degreeLevel")
    private String degreeLevel;

    @JsonProperty(value = "degreeMajor")
    private String degreeMajor;

    @JsonProperty(value = "almaMater")
    private String almaMater;

    @JsonProperty(value = "year")
    private String year;

    /**
     * Extracts the User fields from the UserEducatorDTO.
     *
     * @return a User object containing the extracted User fields.
     */
    public User extractUser() {
        User extractedUser = new User();
        extractedUser.setUserId(this.getUserId());
        extractedUser.setFirstName(this.getFirstName());
        extractedUser.setLastName(this.getLastName());
        extractedUser.setEmail(this.getEmail());
        extractedUser.setPassword(this.getPassword());
        extractedUser.setRole(this.getRole());
        return extractedUser;
    }

    /**
     * Extracts the Educator fields from the UserEducatorDTO.
     *
     * @return an Educator object containing the extracted Educator fields.
     */
    public Educator extractEducator() {
        Educator extractedEducator = new Educator();
        extractedEducator.setEducatorId(this.getUserId());
        extractedEducator.setDegreeLevel(this.getDegreeLevel());
        extractedEducator.setDegreeMajor(this.getDegreeMajor());
        extractedEducator.setAlmaMater(this.getAlmaMater());
        extractedEducator.setYear(this.getYear());
        return extractedEducator;
    }
}
