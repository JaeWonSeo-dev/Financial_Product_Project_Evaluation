package com.portfolio.finance.dto;

import com.portfolio.finance.domain.ProjectEvaluationGrade;

public record ProjectComparisonPoint(
        Long projectId,
        String projectName,
        double npv,
        double irr,
        double riskScore,
        ProjectEvaluationGrade grade
) {
}
