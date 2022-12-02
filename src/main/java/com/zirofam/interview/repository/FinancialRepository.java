package com.zirofam.interview.repository;

import com.zirofam.interview.domain.FinancialEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.persistence.LockModeType;
import java.math.BigDecimal;
import java.util.Optional;

@Repository
public interface FinancialRepository extends JpaRepository<FinancialEntity, String> {
    @Lock(LockModeType.PESSIMISTIC_READ)
    Optional<FinancialEntity> findByUser(String userId);

    @Modifying(clearAutomatically = true)
    @Query(
            "update FinancialEntity as fe set fe.amount= :amount ,fe.status= :status where fe.user= :user")
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    void updateFinancialEntityByUserId(
            @Param("amount") BigDecimal amount,
            @Param("status") String status,
            @Param("user") String user);

    @Modifying(clearAutomatically = true)
    @Query(
            "update FinancialEntity as fe set fe.amount= :amount ,fe.status= :status , fe.user= :user where fe.id= :id")
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    void updateFinancialEntityById(
            @Param("amount") BigDecimal amount,
            @Param("status") String status,
            @Param("user") String user,
            @Param("id") String id);
}
