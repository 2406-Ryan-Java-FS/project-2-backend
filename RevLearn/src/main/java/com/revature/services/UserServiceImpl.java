package com.revature.services;

import com.revature.exceptions.BadRequestException;
import com.revature.exceptions.ConflictException;
import com.revature.exceptions.UnauthorizedException;
import com.revature.models.Educator;
import com.revature.models.User;
import com.revature.models.dtos.UserEducator;
import com.revature.models.enums.Role;
import com.revature.repositories.UserRepository;
import com.revature.services.PasswordEncrypter;
import com.revature.models.dtos.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Persists a User to the repository.
     *
     * @param user The User to be added.
     * @return The persisted User including its newly assigned userId.
     * @throws ConflictException if there's already a User with the given email.
     */
    public User addUser(User user) {
    	boolean signUpStatus = true;
        String signUpStatusMessage = "";

        if (user.getEmail() == null || user.getEmail().isEmpty()) {
            throw new BadRequestException("Email is required.");
        }

        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            throw new BadRequestException("Password is required.");
        }

        if (userRepository.existsByEmail(user.getEmail())) {
            throw new ConflictException("Email already exists.");
        }

        if (user.getRole() == null || !user.getRole().equals(Role.educator)) {
            user.setRole(Role.student);
        }

        try {
            // Encrypt the password and save the user
            String encryptedPassword = PasswordEncrypter.encryptPassword(user.getPassword());
            user.setPassword(encryptedPassword);
            return userRepository.save(user);
        } catch (Exception e) {
            throw new RuntimeException("Internal error occurred during sign up", e);
        }
    }

    /**
     * Retrieves a User from the repository given its userId.
     *
     * @param userId The userId of a User.
     * @return The associated User object.
     * @throws BadRequestException if the userId is invalid.
     */
    public User getUser(Integer userId) {

        if (userId == null || !userRepository.existsById(userId)) {
            throw new BadRequestException("User Id is invalid.");
        }
        return userRepository.findByUserId(userId);
    }

    /**
     * Checks if a user with the given userId exists in the repository.
     *
     * @param userId the userId of the User to check.
     * @return true if a User with the given userId exists, false otherwise.
     */
    public boolean isUser(Integer userId) {

        return userRepository.existsById(userId);
    }

    /**
     * Verifies a User login.
     *
     * @param user User object containing the email and password to verify.
     * @return The userId of the verified User.
     * @throws UnauthorizedException if the email and/or password are invalid.
     */
    public Integer verifyUser(User user) {

       try {  
    	   User existingUser = userRepository.findByEmail(user.getEmail());
    	   String encryptedPass = PasswordEncrypter.encryptPassword(user.getPassword());

        if (existingUser != null && existingUser.getPassword().equals(encryptedPass)) {
            return existingUser.getUserId();
        }
        throw new UnauthorizedException("Invalid login credentials");
    }
        catch (Exception e) {
        throw new RuntimeException("Internal error occurred during sign up", e);
}
}

    /**
     * Merges data from User and Educator entities into a UserEducatorDTO.
     *
     * @param user     the User entity to be combined
     * @param educator the Educator entity to be combined
     * @return a UserEducatorDTO that combines data from both the User and Educator entities
     */
    public UserEducator combineUserAndEducator(User user, Educator educator) {
        UserEducator dto = new UserEducator();
        dto.setUser(user);
        if (educator != null) {
            educator.setEducatorId(user.getUserId());
        }
        dto.setEducator(educator);
        return dto;
    }
}
