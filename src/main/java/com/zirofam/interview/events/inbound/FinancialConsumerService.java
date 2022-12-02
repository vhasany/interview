package com.zirofam.interview.events.inbound;

import com.zirofam.interview.config.AppConstants;
import com.zirofam.interview.domain.enumeration.AccountingStatus;
import com.zirofam.interview.events.dto.FinancialDto;
import com.zirofam.interview.service.FinancialService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.CannotAcquireLockException;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import javax.persistence.LockTimeoutException;
import java.math.BigDecimal;
import java.util.function.BiFunction;

@Service
@Slf4j
@RequiredArgsConstructor
public class FinancialConsumerService {

  private final FinancialService financialService;
  BiFunction<BigDecimal, BigDecimal, Boolean> checkAmount =
      (entityAmount, dtoAmount) -> {
        BigDecimal sum = entityAmount.add(dtoAmount);
        if (sum.doubleValue() > AppConstants.FINANCIAL_ENTITY_MAX_AMOUNT || sum.doubleValue() < 0) {
          return Boolean.FALSE;
        }
        return Boolean.TRUE;
      };

  @KafkaListener(
      topics = "${financial.topic-name}",
      containerFactory = "concurrentFinancialListenerContainerFactory")
  public void consume(FinancialDto dto) {
    financialService
        .findByUser(dto.getUser())
        .ifPresent(
            financialEntity -> {
              if (checkAmount.apply(financialEntity.getAmount(), dto.getAmount())) {
                financialEntity.getAmount().add(dto.getAmount());
                financialEntity.setStatus(AccountingStatus.valueOf(dto.getStatus()));
                try {
                  financialService.updateUser(financialEntity);
                } catch (CannotAcquireLockException | LockTimeoutException exception) {
                    log.warn("retry or send to DLT");
                }
              }
            });
  }
}
