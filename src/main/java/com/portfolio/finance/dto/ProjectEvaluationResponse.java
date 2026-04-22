package com.portfolio.finance.dto;

import com.portfolio.finance.domain.ProjectEvaluationGrade;
import java.util.List;

public record ProjectEvaluationResponse(
        Long projectId,
        String projectName,
        double npv,
        double irr,
        double paybackPeriod,
        double riskScore,
        double varEstimate,
        ProjectEvaluationGrade grade,
        List<ScenarioMetric> scenarios,
        List<SensitivityPoint> sensitivities,
        List<Double> netCashFlows
) {}
