package com.revature.DTO;

import java.sql.Timestamp;
import java.util.Date;
import lombok.Data;

@Data
public class QuizAttemptsDTO {

    private Integer quizAttempts_id;

	private Integer student_id;

    private Integer quiz_id;
	
	private Timestamp attempt_date;
	
	private Double score;
}
