package com.revature.services;

import com.revature.exceptions.*;
import com.revature.models.Educator;
import com.revature.models.User;
import com.revature.models.dtos.UserEducator;
import com.revature.models.enums.Role;
import com.revature.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class UserServiceImpl implements UserService {

    UserRepository userRepository;

    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

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
     * @throws InternalServerErrorException if the password encryption fails.
     */
    public User addUser(User user) {

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
            String encodedPassword = passwordEncoder.encode(user.getPassword());
            user.setPassword(encodedPassword);
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
     * @throws InternalServerErrorException if the password encryption fails.
     */
    public Integer verifyUser(User user) {

        try {
            User existingUser = userRepository.findByEmail(user.getEmail());
            String encodedPassword = passwordEncoder.encode(user.getPassword());

            if (existingUser != null && existingUser.getPassword().equals(encodedPassword)) {
                return existingUser.getUserId();
            }
            throw new UnauthorizedException("Invalid login credentials");
        }
        catch (Exception e) {
            throw new RuntimeException("Internal error occurred during sign in", e);
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

    /**
     * Loads a User by their email.
     *
     * @param email the email of the User to load.
     * @return a UserDetails object representing the User.
     * @throws UsernameNotFoundException if the User is not found.
     */
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email);
        if (user != null) {
            return org.springframework.security.core.userdetails.User.builder()
                    .username(user.getEmail())
                    .password(user.getPassword())
                    .roles(getRoles(user))
                    .build();
        } else {
            throw new UsernameNotFoundException(email);
        }
    }

    /**
     * Retrieves the roles of a User.
     *
     * @param user the User to retrieve roles from.
     * @return an array of roles as Strings.
     */
    public String[] getRoles(User user) {
        if (user.getRole() == null) {
            return new String[]{"STUDENT"};
        }
        return new String[]{"EDUCATOR"};
    }
}
