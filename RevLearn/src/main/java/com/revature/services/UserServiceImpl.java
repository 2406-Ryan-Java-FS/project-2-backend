package com.revature.services;

import com.revature.DTO.UserEducatorDTO;
import com.revature.exceptions.BadRequestException;
import com.revature.exceptions.ConflictException;
import com.revature.exceptions.UnauthorizedException;
import com.revature.models.Educator;
import com.revature.models.User;
import com.revature.models.enums.Role;
import com.revature.repositories.UserRepository;
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

        return userRepository.save(user);
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

        User existingUser = userRepository.findByEmail(user.getEmail());

        if (existingUser != null && existingUser.getPassword().equals(user.getPassword())) {
            return existingUser.getUserId();
        } else {
            throw new UnauthorizedException("Invalid login credentials");
        }
    }

    /**
     * Combines data from User and Educator entities into a UserEducatorDTO.
     *
     * @param user     the User entity
     * @param educator the Educator entity
     * @return a UserEducatorDTO containing combined data from both entities
     */
    public UserEducatorDTO combineUserAndEducator(User user, Educator educator) {
        UserEducatorDTO dto = new UserEducatorDTO();
        dto.setUserId(user.getUserId());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setEmail(user.getEmail());
        dto.setPassword(user.getPassword());
        dto.setRole(user.getRole());
        dto.setDegreeLevel(educator.getDegreeLevel());
        dto.setDegreeMajor(educator.getDegreeMajor());
        dto.setAlmaMater(educator.getAlmaMater());
        dto.setYear(educator.getYear());
        return dto;
    }
}
