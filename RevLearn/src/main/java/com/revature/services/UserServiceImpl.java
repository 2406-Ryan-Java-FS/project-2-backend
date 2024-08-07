package com.revature.services;

import com.revature.exceptions.BadRequestException;
import com.revature.exceptions.ConflictException;
import com.revature.exceptions.InternalServerErrorException;
import com.revature.exceptions.UnauthorizedException;
import com.revature.models.Educator;
import com.revature.models.User;
import com.revature.models.dtos.UserEducator;
import com.revature.models.enums.Role;
import com.revature.repositories.UserRepository;
import com.revature.util.Help;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.revature.services.passutil.PasswordEncrypter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger logger= LoggerFactory.getLogger(UserServiceImpl.class);

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
     * @throws InternalServerErrorException if the password encryption fails.
     */
    public User addUser(User user) {

        if(user==null) throw new BadRequestException("user is null. Please provide one to addUser()");

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
        } catch (NoSuchAlgorithmException e) {
            throw new InternalServerErrorException("Password encryption error, please try again.");
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
    public Integer verifyUser(User user)
    {
        try
        {
            logger.info("given user="+user);

            User existingUser = userRepository.findByEmail(user.getEmail());
            logger.info("existingUser="+Help.json(existingUser,true,false));

            String encryptedPass = PasswordEncrypter.encryptPassword(user.getPassword());

            if (existingUser != null && existingUser.getPassword().equals(encryptedPass))
            {
                logger.info("Returning existing user");
                return existingUser.getUserId();
            }

            throw new UnauthorizedException("Invalid login credentials");
        }
        catch (NoSuchAlgorithmException e)
        {
            throw new InternalServerErrorException("Password encryption error, please try again.");
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
