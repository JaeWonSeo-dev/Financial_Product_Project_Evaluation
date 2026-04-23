package com.portfolio.finance.dto;

import java.util.List;

public record DashboardResponse(
        long totalProducts,
        long totalProjects,
        double averageReturnRate,
        double averageRiskScore,
        List<ProjectEvaluationResponse> recentEvaluations,
        List<String> productNames,
        List<Double> productReturns,
        List<Double> productRiskScores,
        List<ProjectComparisonPoint> projectComparisons
) {
}
