package com.revature.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.revature.models.Quiz;
import com.revature.models.QuizAttempts;

@Repository
public interface QuizAttemptsRepository extends JpaRepository<QuizAttempts,Integer>{

	List<QuizAttempts> findAllByQuiz_QuizId(Integer quiz_id);

	List<QuizAttempts> findAllByStudent_UserId(Integer student_id);

	List<QuizAttempts> findByStudent_UserIdAndQuiz_QuizId(Integer student_id, Integer quiz_id);

	void deleteAllByQuiz_QuizId(Integer quiz_id);

	void deleteAllByStudent_UserId(Integer student_id);
	
	void deleteAllByQuiz_QuizIdAndStudent_UserId(Integer quiz_id, Integer student_id);
	
	int countByQuiz_QuizIdAndStudent_UserId(Integer quiz_id, Integer student_id);

}




