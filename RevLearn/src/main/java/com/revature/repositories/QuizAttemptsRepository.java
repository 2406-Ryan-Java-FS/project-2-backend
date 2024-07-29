package com.revature.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.revature.models.QuizAttempts;

@Repository
public interface QuizAttemptsRepository extends JpaRepository<QuizAttempts,Integer>{

}




