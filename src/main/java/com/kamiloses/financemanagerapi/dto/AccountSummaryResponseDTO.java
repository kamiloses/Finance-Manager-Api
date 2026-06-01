package com.kamiloses.financemanagerapi.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Map;


@Setter @Getter
@AllArgsConstructor
@NoArgsConstructor
public class AccountSummaryResponseDTO {

    private BigDecimal totalIncome;
    private BigDecimal totalExpense;
    private Map<String, BigDecimal> expenseByCategory;
}
