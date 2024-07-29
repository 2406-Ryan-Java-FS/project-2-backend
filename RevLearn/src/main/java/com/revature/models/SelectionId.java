package com.revature.models;

import java.io.Serializable;

import lombok.Data;

// Composite key class
@Data
public class SelectionId implements Serializable {
    
    private int choiceId;

    private int attemptId;

    public SelectionId(int choiceId, int attemptId) {
        this.choiceId = choiceId;
        this.attemptId = attemptId;
    }
}