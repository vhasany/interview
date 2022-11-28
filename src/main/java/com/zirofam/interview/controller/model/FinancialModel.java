package com.zirofam.interview.controller.model;

import com.zirofam.interview.domain.enumeration.AccountingStatus;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class FinancialModel {

    private String id;

    private AccountingStatus status;

    private String user;

    private BigDecimal amount;

}
