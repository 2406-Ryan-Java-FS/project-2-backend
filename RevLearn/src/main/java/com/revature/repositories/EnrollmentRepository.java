package com.revature.repositories;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.revature.models.Enrollment;
import com.revature.models.enums.PayStatus;

import java.util.List;
import java.util.Optional;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Integer>{



    //method to update paymentStatus field in Enrollment
    @Modifying
    @Transactional
    @Query("update Enrollment e set e.paymentStatus = :thePaymentStatus where e.enrollmentId = :theEnrollmentId")
    int updateEnrollmentPaymentStatusById(@Param("theEnrollmentId") Integer theEnrollmentId, @Param("thePaymentStatus")PayStatus thePaymentStatus);

    Optional<Enrollment> findByStudentIdAndCourseId(Integer theStudentId, Integer theCourseId);

    List<Enrollment> findByStudentId(Integer theStudentId);

    List<Enrollment> findByCourseId(Integer theCourseId);

    List<Enrollment> findByPaymentStatus(PayStatus thePaymentStatus);

    List<Enrollment> findByStudentIdAndPaymentStatus(int theStudentId, PayStatus thePaymentStatus);
}