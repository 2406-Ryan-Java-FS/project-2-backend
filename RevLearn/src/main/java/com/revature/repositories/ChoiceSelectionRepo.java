package com.revature.repositories;

import com.revature.models.ChoiceSelection;
import com.revature.models.SelectionId;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChoiceSelectionRepo extends JpaRepository<ChoiceSelection, SelectionId> {
    List<ChoiceSelection> findByAttemptId(int attemptId);
}
