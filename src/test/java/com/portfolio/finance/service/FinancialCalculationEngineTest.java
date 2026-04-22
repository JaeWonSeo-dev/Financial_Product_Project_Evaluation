package com.portfolio.finance.service;

import com.portfolio.finance.domain.ProjectEvaluationGrade;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class FinancialCalculationEngineTest {
    private final FinancialCalculationEngine engine = new FinancialCalculationEngine();

    @Test
    void calculateNpvShouldReturnPositiveValueForProfitableCase() {
        double npv = engine.calculateNpv(1000, List.of(400.0, 450.0, 500.0), 10.0);
        assertThat(npv).isGreaterThan(0);
    }

    @Test
    void calculateIrrShouldBeNearExpectedRange() {
        double irr = engine.calculateIrr(1000, List.of(500.0, 500.0, 400.0));
        assertThat(irr).isBetween(18.0, 22.0);
    }

    @Test
    void calculatePaybackShouldReturnFractionalYears() {
        double payback = engine.calculatePaybackPeriod(1000, List.of(300.0, 400.0, 500.0));
        assertThat(payback).isBetween(2.5, 2.7);
    }

    @Test
    void scenarioAndSensitivityShouldProduceExpectedPointCounts() {
        assertThat(engine.scenarioAnalysis(1000, List.of(300.0, 400.0, 500.0), 9.0)).hasSize(3);
        assertThat(engine.sensitivityAnalysis(1000, List.of(300.0, 400.0, 500.0), 9.0)).hasSize(5);
    }

    @Test
    void gradeShouldReflectProfitabilityAndRisk() {
        assertThat(engine.grade(120.0, 15.0, 30.0)).isEqualTo(ProjectEvaluationGrade.EXCELLENT);
        assertThat(engine.grade(40.0, 9.0, 55.0)).isEqualTo(ProjectEvaluationGrade.NORMAL);
        assertThat(engine.grade(-20.0, 4.0, 80.0)).isEqualTo(ProjectEvaluationGrade.RISKY);
    }
}
