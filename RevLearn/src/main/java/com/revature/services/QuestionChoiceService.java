package com.revature.services;

import com.revature.models.QuestionChoice;
import java.util.List;

public interface QuestionChoiceService {

    public QuestionChoice addChoice(QuestionChoice c);
    public QuestionChoice getChoice(int id);
    public List<QuestionChoice> getAllChoices();
    public QuestionChoice updateChoice(QuestionChoice change);
    public boolean deleteChoice(int id);
    public List<QuestionChoice> getQuestionChoices(int questionId);
    public QuestionChoice getCorrectAnswer(int questionId);
}
