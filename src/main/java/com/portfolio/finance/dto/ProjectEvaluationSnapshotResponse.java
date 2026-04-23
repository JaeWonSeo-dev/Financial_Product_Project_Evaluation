package com.portfolio.finance.dto;

import com.portfolio.finance.domain.ProjectEvaluationGrade;

import java.time.LocalDateTime;

public record ProjectEvaluationSnapshotResponse(
        Long id,
        Long projectId,
        String projectName,
        double npv,
        double irr,
        double paybackPeriod,
        double riskScore,
        double varEstimate,
        ProjectEvaluationGrade grade,
        LocalDateTime evaluatedAt
) {
}
