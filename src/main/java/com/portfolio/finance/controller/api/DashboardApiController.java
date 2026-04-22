package com.portfolio.finance.controller.api;

import com.portfolio.finance.dto.DashboardResponse;
import com.portfolio.finance.service.DashboardService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardApiController {
    private final DashboardService dashboardService;
    public DashboardApiController(DashboardService dashboardService) { this.dashboardService = dashboardService; }
    @GetMapping public DashboardResponse getDashboard() { return dashboardService.getDashboard(); }
}
