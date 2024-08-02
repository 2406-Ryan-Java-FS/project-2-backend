package com.revature.models;

import java.io.Serializable;

import jakarta.persistence.Embeddable;
import lombok.Data;

// Composite key class
@Data
@Embeddable
public class SelectionId implements Serializable {
    
    private int choiceId;
    private int attemptId;

}