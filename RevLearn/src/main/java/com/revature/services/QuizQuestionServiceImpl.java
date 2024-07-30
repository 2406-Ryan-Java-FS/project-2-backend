package com.revature.services;

import com.revature.models.QuizQuestion;
import com.revature.models.dtos.QuizQuestionDTO;
import com.revature.repositories.QuizQuestionRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuizQuestionServiceImpl implements QuizQuestionService {

    
    QuizQuestionRepo qr;

    public QuizQuestionServiceImpl(QuizQuestionRepo qr) {
        this.qr = qr;
    }

    @Override
    public QuizQuestion addQuestion(QuizQuestion q) {
        try {
            return qr.save(q);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public QuizQuestionDTO addQuestionDTO(QuizQuestionDTO q, int quizId) {
        QuizQuestion newQuestion = new QuizQuestion();
        newQuestion.setQuestionText(q.getQuestion_text());
        newQuestion.setQuizId(quizId);
        qr.save(newQuestion);
        return q;
    }

    @Override
    public QuizQuestion getQuestion(int id) {
        return qr.findById(id).orElseGet(QuizQuestion::new);
    }

    @Override
    public List<QuizQuestion> getAllQuestions() {
        return qr.findAll();
    }

    @Override
    public QuizQuestion updateQuestion(QuizQuestion change) {
        return qr.save(change);
    }

    @Override
    public boolean deleteQuestion(int id) {
        try {
            QuizQuestion q = getQuestion(id);
            if(q.getId() != 0) {
                qr.deleteById(id);
                return true;
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public List<QuizQuestion> getQuizQuestions(int quizId) {
        return qr.findByQuizId(quizId);
    }
}
