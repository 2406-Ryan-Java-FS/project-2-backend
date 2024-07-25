package com.revature.repositories;

import com.revature.models.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends JpaRepository<Users,Integer> {

    Users findFirstByUserEmail(String userEmail);
}