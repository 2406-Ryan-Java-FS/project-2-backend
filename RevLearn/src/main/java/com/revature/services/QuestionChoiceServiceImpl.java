package com.revature.services;

import com.revature.models.QuestionChoice;
import com.revature.models.QuizQuestion;
import com.revature.repositories.QuestionChoiceRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuestionChoiceServiceImpl implements QuestionChoiceService {

    
    QuestionChoiceRepo qcr;

    @Autowired
    public QuestionChoiceServiceImpl(QuestionChoiceRepo qcr) {
        this.qcr = qcr;
    }

    @Override
    public QuestionChoice addChoice(QuestionChoice c) {
        try {
            return qcr.save(c);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public QuestionChoice getChoice(int id) {
        return qcr.findById(id).orElseGet(QuestionChoice::new);
    }

    @Override
    public List<QuestionChoice> getAllChoices() {
        return qcr.findAll();
    }

    @Override
    public QuestionChoice updateChoice(QuestionChoice change) {
        return qcr.save(change);
    }

    @Override
    public boolean deleteChoice(int id) {
        try {
            QuestionChoice c = getChoice(id);
            if(c.getId() != 0) {
                qcr.deleteById(id);
                return true;
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public List<QuestionChoice> getQuestionChoices(int questionId) {
        return qcr.findByQuestionId(questionId);
    }

//    @Override
//    public QuestionChoice getCorrectAnswer(int questionId) {
//        // TODO Auto-generated method stub
//        throw new UnsupportedOperationException("Unimplemented method 'getCorrectAnswer'");
//    }

     @Override
     public QuestionChoice getCorrectAnswer(int questionId) {
         List<QuestionChoice> correctAnswer = qcr.findByQuestionIdAndCorrect(questionId, true);
         if (!correctAnswer.isEmpty()) {
             return correctAnswer.get(0);
         } else {
             return new QuestionChoice();
         }
     }
}
