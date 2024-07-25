package com.revature.services;

import com.revature.models.Quiz;
import com.revature.repositories.QuizRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuizService {

    QuizRepository qr;

    @Autowired
    public QuizService(QuizRepository quizRepository){
        this.qr = quizRepository;
    }

    public List<Quiz> getAllQuizzes(){
        return qr.findAll();
    }

    public Quiz getQuizById(int id){
        return qr.findByQuizId(id);
    }

    public Quiz addQuiz(Quiz quiz){
        return qr.save(quiz);
    }

    public Quiz updateQuiz(Quiz update){
        return qr.save(update);
    }

    public boolean deleteQuiz(int id) {
        try {
            qr.deleteById(id);
            return true;
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return false;
        }
    }


}
