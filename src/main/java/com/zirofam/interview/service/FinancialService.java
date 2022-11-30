package com.zirofam.interview.service;

import com.zirofam.interview.domain.FinancialEntity;
import com.zirofam.interview.repository.FinancialRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class FinancialService {

  private final FinancialRepository repository;

  public FinancialEntity save(FinancialEntity entity) {
    return repository.save(entity);
  }

  public List<FinancialEntity> findAll() {
    return repository.findAll();
  }

  public Optional<FinancialEntity> findById(String id) {
    return repository.findById(id);
  }

  public void deleteById(String id) {
    repository.deleteById(id);
  }

  public Optional<FinancialEntity> findByUser(String userId) {
    return repository.findByUser(userId);
  }
}
