package com.zirofam.interview.service;

import com.zirofam.interview.domain.FinancialEntity;
import com.zirofam.interview.repository.FinancialRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.CannotAcquireLockException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.LockTimeoutException;
import javax.persistence.PessimisticLockException;
import java.math.BigDecimal;
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

  public void deleteById(String id) throws PessimisticLockException {
    repository.deleteById(id);
  }

  public Optional<FinancialEntity> findByUser(String userId) {
    return repository.findByUser(userId);
  }

  public void updateUser(FinancialEntity financialEntity)
      throws CannotAcquireLockException, LockTimeoutException {
    repository.updateFinancialEntityByUserId(
        financialEntity.getAmount(), financialEntity.getStatus().name(), financialEntity.getUser());
  }
  public void update(FinancialEntity financialEntity)
          throws CannotAcquireLockException, LockTimeoutException {
    repository.updateFinancialEntityById(
        financialEntity.getAmount(),
        financialEntity.getStatus().name(),
        financialEntity.getUser(),
        financialEntity.getId());
  }
}
