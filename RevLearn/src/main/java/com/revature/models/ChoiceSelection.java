package com.revature.models;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;

@Entity
@IdClass(SelectionId.class)
@Table(name = "choiceselections", schema = "project2")
@Data
public class ChoiceSelection {

    @Id
    @Column(name = "choice_id")
    private int choiceId;

    @Id
    @Column(name = "attempt_id")
    private int attemptId;
}

// Composite key class
@Data
class SelectionId implements Serializable {
    private int choiceId;
    private int attemptId;
}
