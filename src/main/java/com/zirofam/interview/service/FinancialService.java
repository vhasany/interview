package com.zirofam.interview.service;

import com.zirofam.interview.domain.FinancialEntity;
import com.zirofam.interview.repository.FinancialRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class FinancialService {

    private final FinancialRepository repository;

    public FinancialService(FinancialRepository repository) {
        this.repository = repository;
    }

    public FinancialEntity save(FinancialEntity entity) {
        return repository.save(entity);
    }

    public List<FinancialEntity> findAll() {
        return repository.findAll();
    }
}
