package com.revature.models;

import java.io.Serializable;

import lombok.Data;

// Composite key class
@Data
public class SelectionId implements Serializable {
    
    private int choiceId;

    private int attemptId;
}