package com.portfolio.finance.controller.api;

import com.portfolio.finance.domain.ProjectEvaluationGrade;
import com.portfolio.finance.dto.DashboardResponse;
import com.portfolio.finance.dto.ProjectComparisonPoint;
import com.portfolio.finance.dto.ProjectEvaluationResponse;
import com.portfolio.finance.dto.ScenarioMetric;
import com.portfolio.finance.dto.SensitivityPoint;
import com.portfolio.finance.service.DashboardService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class DashboardApiResponseTest {

    private MockMvc mockMvc;
    private DashboardService dashboardService;

    @BeforeEach
    void setUp() {
        dashboardService = mock(DashboardService.class);
        DashboardApiController controller = new DashboardApiController(dashboardService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void getDashboard_shouldReturnProjectComparisonMetrics() throws Exception {
        when(dashboardService.getDashboard()).thenReturn(new DashboardResponse(
                4,
                3,
                7.42,
                51.2,
                List.of(new ProjectEvaluationResponse(
                        1L,
                        "디지털 보험 플랫폼",
                        12500000.12,
                        13.4,
                        2.8,
                        34.2,
                        9900000.0,
                        ProjectEvaluationGrade.EXCELLENT,
                        List.of(new ScenarioMetric("기준", 12500000.12, 13.4, 2.8)),
                        List.of(new SensitivityPoint(8.5, 12500000.12)),
                        List.of(30000000.0, 36000000.0)
                )),
                List.of("국공채 안정형", "성장형 펀드"),
                List.of(4.2, 9.8),
                List.of(25.0, 75.0),
                List.of(new ProjectComparisonPoint(1L, "디지털 보험 플랫폼", 12500000.12, 13.4, 34.2, ProjectEvaluationGrade.EXCELLENT))
        ));

        mockMvc.perform(get("/api/dashboard"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalProducts").value(4))
                .andExpect(jsonPath("$.recentEvaluations[0].projectName").value("디지털 보험 플랫폼"))
                .andExpect(jsonPath("$.projectComparisons[0].projectName").value("디지털 보험 플랫폼"))
                .andExpect(jsonPath("$.projectComparisons[0].grade").value("EXCELLENT"))
                .andExpect(jsonPath("$.productNames[1]").value("성장형 펀드"));
    }
}
