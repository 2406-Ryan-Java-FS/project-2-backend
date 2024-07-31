package com.revature.services;

import com.revature.exceptions.BadRequestException;
import com.revature.models.Quiz;
import com.revature.models.dtos.QuizDTO;

import java.util.List;

public interface QuizService {
    public Quiz addQuiz(QuizDTO q) throws BadRequestException;
    public Quiz getQuizById(int id) throws BadRequestException;
    public List<Quiz> getAllQuizzes();
    public Quiz updateQuiz(Quiz q) throws BadRequestException;
    public boolean deleteQuizById(int id) throws BadRequestException;
    public List<Quiz> getAllQuizzesByCourse(int courseId) throws BadRequestException;

}
