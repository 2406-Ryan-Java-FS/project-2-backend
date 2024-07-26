package com.revature.repositories;

import com.revature.models.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EducatorRepository extends JpaRepository<Users,Integer> {

	Users findByUserId(int userId);
	Users findByEmail(String newEmail);
}