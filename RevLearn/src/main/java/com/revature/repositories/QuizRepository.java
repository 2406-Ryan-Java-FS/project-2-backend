package com.revature.repositories;

import com.revature.models.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuizRepository extends JpaRepository<Quiz, Integer> {
    Quiz findByQuizId(int quizId);
    List<Quiz> findAllByCourseId(int courseId);
}
