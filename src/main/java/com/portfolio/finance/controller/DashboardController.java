package com.portfolio.finance.controller;

import com.portfolio.finance.domain.ProductType;
import com.portfolio.finance.domain.ProjectStatus;
import com.portfolio.finance.domain.RiskGrade;
import com.portfolio.finance.dto.ProductRequest;
import com.portfolio.finance.dto.ProjectRequest;
import com.portfolio.finance.service.DashboardService;
import com.portfolio.finance.service.FinancialProductService;
import com.portfolio.finance.service.ProjectEvaluationService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class DashboardController {
    private final DashboardService dashboardService;
    private final FinancialProductService productService;
    private final ProjectEvaluationService projectService;
    public DashboardController(DashboardService dashboardService, FinancialProductService productService, ProjectEvaluationService projectService) {
        this.dashboardService = dashboardService; this.productService = productService; this.projectService = projectService;
    }
    @GetMapping("/")
    public String dashboard(Model model) {
        model.addAttribute("dashboard", dashboardService.getDashboard());
        model.addAttribute("products", productService.findAll());
        model.addAttribute("projects", projectService.findAll());
        model.addAttribute("productTypes", ProductType.values());
        model.addAttribute("riskGrades", RiskGrade.values());
        model.addAttribute("projectStatuses", ProjectStatus.values());
        model.addAttribute("productForm", new ProductRequest());
        ProjectRequest projectRequest = new ProjectRequest();
        projectRequest.setCashFlows(List.of(new com.portfolio.finance.dto.CashFlowInput(), new com.portfolio.finance.dto.CashFlowInput(), new com.portfolio.finance.dto.CashFlowInput()));
        model.addAttribute("projectForm", projectRequest);
        return "dashboard";
    }
}
