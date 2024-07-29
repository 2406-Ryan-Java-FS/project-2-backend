package com.revature.services;

import com.revature.models.ChoiceSelection;

import java.util.List;

public interface ChoiceSelectionService {
    public ChoiceSelection addSelection(ChoiceSelection cs);
    public List<ChoiceSelection> getAllSelections();
    public List<ChoiceSelection> getAttemptSelections(int attemptId);
}
