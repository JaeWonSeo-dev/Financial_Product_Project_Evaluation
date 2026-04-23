package com.portfolio.finance.dto;

import com.portfolio.finance.domain.ProjectStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record ProjectDetailResponse(
        Long id,
        String name,
        String category,
        BigDecimal initialInvestment,
        double discountRate,
        ProjectStatus status,
        String owner,
        LocalDate startDate,
        LocalDate endDate,
        String description,
        LocalDateTime createdAt,
        List<ProjectCashFlowResponse> cashFlows
) {
}
