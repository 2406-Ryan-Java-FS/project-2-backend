package com.revature.services;

import com.revature.models.QuestionChoice;
import com.revature.models.dtos.QuestionChoiceDTO;
import com.revature.repositories.QuestionChoiceRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuestionChoiceServiceImpl implements QuestionChoiceService {

    
    QuestionChoiceRepo qcr;

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
    public QuestionChoiceDTO addChoiceDTO(QuestionChoiceDTO csDTO, int questionId) {
        QuestionChoice myChoice = new QuestionChoice();
        myChoice.setChoiceText(csDTO.getText());
        myChoice.setCorrect(csDTO.getCorrect());
        myChoice.setQuestionId(questionId);
        qcr.save(myChoice);
        return csDTO;
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
