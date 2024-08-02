package com.revature.services;

import com.revature.exceptions.BadRequestException;
import com.revature.models.QuizQuestion;
import com.revature.models.dtos.QuizQuestionDTO;

import java.util.List;

public interface QuizQuestionService {

    public QuizQuestion addQuestion(QuizQuestion q) throws BadRequestException;
    public QuizQuestion addQuestionDTO(QuizQuestionDTO q, int quizId) throws BadRequestException;
    public QuizQuestion getQuestion(int id);
    public List<QuizQuestion> getAllQuestions();
    public QuizQuestion updateQuestion(QuizQuestion change);
    public boolean deleteQuestion(int id);
    public List<QuizQuestion> getQuizQuestions(int quizId);
}
