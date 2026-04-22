package com.portfolio.finance.service;

import com.portfolio.finance.domain.ProjectEvaluationGrade;
import com.portfolio.finance.dto.ScenarioMetric;
import com.portfolio.finance.dto.SensitivityPoint;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class FinancialCalculationEngine {

    public double calculateNpv(double initialInvestment, List<Double> netCashFlows, double discountRatePercent) {
        double rate = discountRatePercent / 100.0;
        double npv = -initialInvestment;
        for (int i = 0; i < netCashFlows.size(); i++) {
            npv += netCashFlows.get(i) / Math.pow(1 + rate, i + 1);
        }
        return round(npv);
    }

    public double calculateIrr(double initialInvestment, List<Double> netCashFlows) {
        double guess = 0.1;
        for (int i = 0; i < 200; i++) {
            double f = -initialInvestment;
            double derivative = 0.0;
            for (int t = 0; t < netCashFlows.size(); t++) {
                double cash = netCashFlows.get(t);
                f += cash / Math.pow(1 + guess, t + 1);
                derivative += -(t + 1) * cash / Math.pow(1 + guess, t + 2);
            }
            if (Math.abs(derivative) < 1e-9) break;
            double next = guess - (f / derivative);
            if (Double.isNaN(next) || Double.isInfinite(next)) break;
            if (Math.abs(next - guess) < 1e-7) {
                guess = next;
                break;
            }
            guess = Math.max(-0.99, Math.min(10.0, next));
        }
        return round(guess * 100.0);
    }

    public double calculatePaybackPeriod(double initialInvestment, List<Double> netCashFlows) {
        double cumulative = 0.0;
        for (int i = 0; i < netCashFlows.size(); i++) {
            double before = cumulative;
            cumulative += netCashFlows.get(i);
            if (cumulative >= initialInvestment) {
                double needed = initialInvestment - before;
                double fraction = netCashFlows.get(i) == 0 ? 0 : needed / netCashFlows.get(i);
                return round(i + fraction);
            }
        }
        return -1;
    }

    public List<ScenarioMetric> scenarioAnalysis(double initialInvestment, List<Double> baseCashFlows, double baseDiscountRate) {
        List<ScenarioMetric> scenarios = new ArrayList<>();
        scenarios.add(buildScenario("낙관", initialInvestment, adjustCashFlows(baseCashFlows, 1.15), Math.max(baseDiscountRate - 1.0, 0.1)));
        scenarios.add(buildScenario("기준", initialInvestment, baseCashFlows, baseDiscountRate));
        scenarios.add(buildScenario("비관", initialInvestment, adjustCashFlows(baseCashFlows, 0.85), baseDiscountRate + 1.5));
        return scenarios;
    }

    public List<SensitivityPoint> sensitivityAnalysis(double initialInvestment, List<Double> cashFlows, double baseDiscountRate) {
        List<SensitivityPoint> points = new ArrayList<>();
        for (double delta = -2.0; delta <= 2.0; delta += 1.0) {
            double rate = Math.max(0.1, baseDiscountRate + delta);
            points.add(new SensitivityPoint(round(rate), calculateNpv(initialInvestment, cashFlows, rate)));
        }
        return points;
    }

    public double calculateRiskScore(double discountRate, double irr, double annualizedVolatility, double paybackPeriod) {
        double spreadScore = discountRate > irr ? 55 : 25;
        double volatilityScore = Math.min(annualizedVolatility * 1.8, 25);
        double paybackScore = paybackPeriod < 0 ? 20 : Math.min(paybackPeriod * 4, 20);
        return round(Math.min(100, spreadScore + volatilityScore + paybackScore));
    }

    public double estimateVarStyleLoss(double investment, double annualizedVolatility) {
        double zScore95 = 1.65;
        return round(investment * (annualizedVolatility / 100.0) * zScore95);
    }

    public ProjectEvaluationGrade grade(double npv, double irr, double riskScore) {
        if (npv > 0 && irr >= 12 && riskScore < 45) return ProjectEvaluationGrade.EXCELLENT;
        if (npv > 0 && irr >= 7 && riskScore < 70) return ProjectEvaluationGrade.NORMAL;
        return ProjectEvaluationGrade.RISKY;
    }

    private ScenarioMetric buildScenario(String name, double investment, List<Double> cashFlows, double discountRate) {
        return new ScenarioMetric(name, calculateNpv(investment, cashFlows, discountRate), calculateIrr(investment, cashFlows), calculatePaybackPeriod(investment, cashFlows));
    }

    private List<Double> adjustCashFlows(List<Double> baseCashFlows, double multiplier) {
        return baseCashFlows.stream().map(v -> round(v * multiplier)).toList();
    }

    private double round(double value) { return Math.round(value * 100.0) / 100.0; }
}
