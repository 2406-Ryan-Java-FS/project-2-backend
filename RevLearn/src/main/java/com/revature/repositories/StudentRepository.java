package com.revature.repositories;

import com.revature.models.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends JpaRepository<Users,Integer> {

    Users findByUserId(int newUserId);
    static Users findByEmail(String newEmail) {
		// TODO Auto-generated method stub
		return null;
	}
}