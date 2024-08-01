package com.revature.models;

import jakarta.persistence.*;
import lombok.Data;

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

// // Composite key class
// @Data
// @Embeddable
// class SelectionId implements Serializable {
//     // private int choiceId;
//     // private int attemptId;

//     // public SelectionId(int choiceId, int attemptId) {
//     //     this.choiceId = choiceId;
//     //     this.attemptId = attemptId;
//     // }

//     @Column(name = "choice_id")
//     private int choiceId;

//     @Id
//     @Column(name = "attempt_id")
//     private int attemptId;
// }
