package com.revature.services;

import com.revature.models.Quiz;

import java.util.List;

public interface QuizService {
    public Quiz addQuiz(Quiz q);
    public Quiz getQuizById(int id);
    public List<Quiz> getAllQuizzes();
    public Quiz updateQuiz(Quiz q);
    public boolean deleteQuizById(int id);
    public List<Quiz> getAllQuizzesByCourse(int courseId);

}
