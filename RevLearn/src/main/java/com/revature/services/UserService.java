package com.revature.services;

import com.revature.exceptions.BadRequestException;
import com.revature.exceptions.ConflictException;
import com.revature.exceptions.UnauthorizedException;
import com.revature.models.User;
import com.revature.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Persists a User to the repository.
     *
     * @param user The User to be added.
     * @return The persisted User including its newly assigned user_id.
     * @throws ConflictException if there's already a User with the given email.
     */
    public User addUser(User user) {

        // TODO: check data constraints

        if (userRepository.findByEmail(user.getEmail()) != null) {
            throw new ConflictException("Email already exists.");
        }

        return userRepository.save(user);
    }

    /**
     * Retrieves a User from the repository given its user_id.
     *
     * @param user_id The user_id of a User.
     * @return The associated User object.
     * @throws BadRequestException if the user_id is invalid.
     */
    public User getUser(Integer user_id) {

        if (user_id == null || !userRepository.existsById(user_id)) {
            throw new BadRequestException("User Id is invalid.");
        }
        return userRepository.findByUserId(user_id);
    }

    /**
     * Updates a User in the repository given its user_id.
     *
     * @param user_id The user_id of the User to be updated.
     * @param user   User object containing updated information.
     * @return The updated User object.
     * @throws BadRequestException   if there's an issue with the client's request.
     * @throws UnauthorizedException if trying to change roles without sufficient privileges.
     * @throws ConflictException     if there's already a User with the given email.
     */
    public User updateUser(Integer user_id, User user) {

        // TODO: check data constraints and permissions

        User updatedUser = this.getUser(user_id);
        updatedUser.setEmail(user.getEmail());
        updatedUser.setFirst_name(user.getFirst_name());
        updatedUser.setLast_name(user.getLast_name());
        updatedUser.setPassword(user.getPassword());
        updatedUser.setRole(user.getRole());
        return userRepository.save(updatedUser);
    }

    /**
     * Deletes a User with the given user_id.
     *
     * @param user_id The user_id of the User to be deleted.
     * @throws BadRequestException if the user_id is invalid.
     */
    public boolean deleteUser(Integer user_id) {

        if (user_id == null || !userRepository.existsById(user_id)) {
            throw new BadRequestException("User Id is invalid.");
        }
        userRepository.deleteById(user_id);
        return true;
    }

    /**
     * Retrieves all Users in the repository.
     *
     * @return A list of all Users.
     */
    public List<User> getAllUsers() {

        return userRepository.findAll();
    }
}
