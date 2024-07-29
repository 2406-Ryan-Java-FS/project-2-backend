package com.revature.services;

import com.revature.models.ChoiceSelection;
import com.revature.repositories.ChoiceSelectionRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChoiceSelectionServiceImpl implements ChoiceSelectionService {

    ChoiceSelectionRepo csr;

    public ChoiceSelectionServiceImpl(ChoiceSelectionRepo csr) {
        this.csr = csr;
    }

    @Override
    public ChoiceSelection addSelection(ChoiceSelection cs) {
        try {
            return csr.save(cs);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public List<ChoiceSelection> getAllSelections() {
        return csr.findAll();
    }

    @Override
    public List<ChoiceSelection> getAttemptSelections(int attemptId) {
        return csr.findByAttemptId(attemptId);
    }
}
