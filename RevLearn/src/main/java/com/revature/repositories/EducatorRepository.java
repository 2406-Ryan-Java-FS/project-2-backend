package com.revature.repositories;

import com.revature.models.Educator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EducatorRepository extends JpaRepository<Educator, Integer> {
    Educator findByEducatorId(Integer educatorId);
}
