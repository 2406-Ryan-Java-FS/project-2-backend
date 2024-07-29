package com.revature.services;

import com.revature.exceptions.BadRequestException;
import com.revature.models.Educator;
import com.revature.repositories.EducatorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EducatorServiceImpl implements EducatorService {

    EducatorRepository educatorRepository;
    UserServiceImpl userService;

    @Autowired
    public EducatorServiceImpl(EducatorRepository educatorRepository, UserServiceImpl userService) {
        this.educatorRepository = educatorRepository;
        this.userService = userService;
    }

    /**
     * Persists an Educator to the repository.
     *
     * @param educator The Educator to be added.
     * @return The persisted Educator
     * @throws BadRequestException if the educatorId doesn't match an existing User.
     */
    public Educator addEducator(Educator educator) {

        if (!userService.isUser(educator.getEducatorId())) {
            throw new BadRequestException("Educator Id doesn't match an existing User.");
        }

        return educatorRepository.save(educator);
    }

    /**
     * Retrieves an Educator from the repository given its educatorId.
     *
     * @param educatorId The educatorId of an Educator.
     * @return The associated Educator object.
     * @throws BadRequestException if the educatorId is invalid.
     */
    public Educator getEducator(Integer educatorId) {

        if (educatorId == null || !educatorRepository.existsById(educatorId)) {
            throw new BadRequestException("Educator Id is invalid.");
        }
        return educatorRepository.findByEducatorId(educatorId);
    }
}
