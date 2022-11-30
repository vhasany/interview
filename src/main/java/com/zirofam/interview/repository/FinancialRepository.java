package com.zirofam.interview.repository;


import com.zirofam.interview.domain.FinancialEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FinancialRepository extends JpaRepository<FinancialEntity, String> {
    Optional<FinancialEntity> findByUser(String userId);
}
