package com.revature.repositories;

import com.revature.models.QuestionChoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionChoiceRepo extends JpaRepository<QuestionChoice, Integer>{
    List<QuestionChoice> findByQuestionId(int questionId);
    // List<QuestionChoice> findByQuestionIdAndCorrect(int questionId, boolean correct);
}
