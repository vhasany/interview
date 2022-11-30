package com.zirofam.interview.controller.dto;

import com.zirofam.interview.domain.enumeration.AccountingStatus;
import com.zirofam.interview.util.annotation.EnumValidator;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateFinancialDto {
  @NotNull
  @EnumValidator(enumClass = AccountingStatus.class)
  private String status;

  @NotBlank(message = "user can not be null or empty")
  private String user;

  @DecimalMin("0.00")
  @DecimalMax("1000000.00")
  @NotNull(message = "amount can not be null")
  private BigDecimal amount;
}
