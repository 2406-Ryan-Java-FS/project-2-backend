package com.revature.services;

import com.revature.models.Quiz;
import com.revature.repositories.QuizRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuizServiceImpl implements QuizService{

    QuizRepository qr;

    public QuizServiceImpl(QuizRepository quizRepository){
        this.qr = quizRepository;
    }

    @Override
    public List<Quiz> getAllQuizzes(){
        return qr.findAll();
    }

    @Override
    public Quiz getQuizById(int id){
        return qr.findByQuizId(id);
    }

    @Override
    public Quiz addQuiz(Quiz quiz){
        return qr.save(quiz);
    }

    @Override
    public Quiz updateQuiz(Quiz update){
        return qr.save(update);
    }

    @Override
    public boolean deleteQuizById(int id) {
        try {
            qr.deleteById(id);
            return true;
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return false;
        }
    }
    @Override
    public List<Quiz> getAllQuizzesByCourse(int courseId){
        return qr.findAllByCourseId(courseId);
    }
}
