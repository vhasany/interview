package com.zirofam.interview.events.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FinancialDto {
  private String status;
  private String user;
  private BigDecimal amount;
}
