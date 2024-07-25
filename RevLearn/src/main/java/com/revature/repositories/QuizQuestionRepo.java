package com.revature.repositories;

import com.revature.models.QuizQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuizQuestionRepo extends JpaRepository<QuizQuestion, Integer> {
    List<QuizQuestion> findByQuizId(int quizId);
}
