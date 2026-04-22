package com.portfolio.finance.service;

import com.portfolio.finance.domain.FinancialProduct;
import com.portfolio.finance.dto.DashboardResponse;
import com.portfolio.finance.dto.ProjectEvaluationResponse;
import com.portfolio.finance.repository.EvaluationProjectRepository;
import com.portfolio.finance.repository.FinancialProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DashboardService {
    private final FinancialProductRepository productRepository;
    private final EvaluationProjectRepository projectRepository;
    private final ProjectEvaluationService projectEvaluationService;
    public DashboardService(FinancialProductRepository productRepository, EvaluationProjectRepository projectRepository, ProjectEvaluationService projectEvaluationService) {
        this.productRepository = productRepository; this.projectRepository = projectRepository; this.projectEvaluationService = projectEvaluationService;
    }
    public DashboardResponse getDashboard() {
        List<FinancialProduct> products = productRepository.findAll();
        double avgReturn = products.stream().mapToDouble(FinancialProduct::getExpectedReturnRate).average().orElse(0.0);
        double avgRisk = products.stream().mapToDouble(p -> p.getRiskGrade().getScore() * 25.0).average().orElse(0.0);
        List<ProjectEvaluationResponse> recent = projectRepository.findAll().stream().limit(3).map(p -> projectEvaluationService.evaluate(p.getId())).toList();
        return new DashboardResponse(productRepository.count(), projectRepository.count(), round(avgReturn), round(avgRisk), recent,
                products.stream().map(FinancialProduct::getName).toList(),
                products.stream().map(FinancialProduct::getExpectedReturnRate).map(this::round).toList(),
                products.stream().map(p -> round(p.getRiskGrade().getScore() * 25.0)).toList());
    }
    private double round(double value) { return Math.round(value * 100.0) / 100.0; }
}
