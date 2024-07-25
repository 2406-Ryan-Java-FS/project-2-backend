package com.revature.repositories;

import com.revature.models.PayStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.revature.models.Enrollment;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Integer>{



    //method to update paymentStatus field in Enrollment
    @Modifying
    @Query("update Enrollment e set e.paymentStatus = :thePaymentStatus where e.enrollmentId = :theEnrollmentId")
    int updateEnrollmentPaymentStatusById(@Param("theEnrollmentId") Integer theEnrollmentId, @Param("thePaymentStatus")PayStatus thePaymentStatus);

}