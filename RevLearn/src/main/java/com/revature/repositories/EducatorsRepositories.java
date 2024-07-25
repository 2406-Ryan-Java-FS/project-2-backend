package com.revature.repositories;

import com.revature.models.Educators;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EducatorsRepositories extends JpaRepository<Educators, Integer> {
}
