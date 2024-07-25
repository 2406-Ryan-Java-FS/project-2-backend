package com.revature.services;

import com.revature.exceptions.BadRequestException;
import com.revature.models.Educator;
import com.revature.repositories.EducatorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EducatorService {

    EducatorRepository educatorRepository;
    UserService userService;

    @Autowired
    public EducatorService(EducatorRepository educatorRepository, UserService userService) {
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

    /**
     * Updates an Educator in the repository given its educatorId.
     *
     * @param educatorId The educatorId of the Educator to be updated.
     * @param educator   Educator object containing updated information.
     * @return The updated Educator object.
     */
    public Educator updateEducator(Integer educatorId, Educator educator) {

        Educator updatedEducator = this.getEducator(educatorId);

        if (educator.getDegreeLevel() != null && !educator.getDegreeLevel().isEmpty()) {
            updatedEducator.setDegreeLevel(educator.getDegreeLevel());
        }

        if (educator.getDegreeMajor() != null && !educator.getDegreeMajor().isEmpty()) {
            updatedEducator.setDegreeMajor(educator.getDegreeMajor());
        }

        if (educator.getAlmaMater() != null && !educator.getAlmaMater().isEmpty()) {
            updatedEducator.setAlmaMater(educator.getAlmaMater());
        }

        if (educator.getYear() != null) {
            updatedEducator.setYear(educator.getYear());
        }

        return educatorRepository.save(updatedEducator);
    }

    /**
     * Deletes an Educator with the given educatorId.
     *
     * @param educatorId The educatorId of the Educator to be deleted.
     * @throws BadRequestException if the educatorId is invalid.
     */
    public boolean deleteEducator(Integer educatorId) {

        if (educatorId == null || !educatorRepository.existsById(educatorId)) {
            throw new BadRequestException("Educator Id is invalid.");
        }
        educatorRepository.deleteById(educatorId);
        return true;
    }

    /**
     * Retrieves all Educators in the repository.
     *
     * @return A list of all Educators.
     */
    public List<Educator> getAllEducators() {

        return educatorRepository.findAll();
    }
}
