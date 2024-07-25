package com.revature.services;

import com.revature.models.QuizQuestion;

import java.util.List;

public interface QuizQuestionService {

    public QuizQuestion addQuestion(QuizQuestion q);
    public QuizQuestion getQuestion(int id);
    public List<QuizQuestion> getAllQuestions();
    public QuizQuestion updateQuestion(QuizQuestion change);
    public boolean deleteQuestion(int id);
    public List<QuizQuestion> getQuizQuestions(int quizId);
}
