package com.portfolio.finance.dto;

import com.portfolio.finance.domain.ProjectStatus;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ProjectSummaryResponse(
        Long id,
        String name,
        String category,
        BigDecimal initialInvestment,
        double discountRate,
        ProjectStatus status,
        String owner,
        LocalDate startDate,
        LocalDate endDate,
        int cashFlowCount
) {
}
