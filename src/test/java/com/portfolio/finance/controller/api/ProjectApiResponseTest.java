package com.portfolio.finance.controller.api;

import com.portfolio.finance.domain.ProjectEvaluationGrade;
import com.portfolio.finance.domain.ProjectStatus;
import com.portfolio.finance.dto.ProjectCashFlowResponse;
import com.portfolio.finance.dto.ProjectDetailResponse;
import com.portfolio.finance.dto.ProjectEvaluationSnapshotResponse;
import com.portfolio.finance.dto.ProjectSummaryResponse;
import com.portfolio.finance.service.ProjectEvaluationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ProjectApiResponseTest {

    private MockMvc mockMvc;
    private ProjectEvaluationService service;

    @BeforeEach
    void setUp() {
        service = mock(ProjectEvaluationService.class);
        ProjectApiController controller = new ProjectApiController(service);
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new ApiExceptionHandler())
                .build();
    }

    @Test
    void findAll_shouldReturnProjectSummaryResponse() throws Exception {
        when(service.findAllSummaries()).thenReturn(List.of(
                new ProjectSummaryResponse(
                        1L,
                        "디지털 보험 플랫폼",
                        "보험",
                        BigDecimal.valueOf(100000000),
                        8.5,
                        ProjectStatus.REVIEW,
                        "전략기획팀",
                        LocalDate.of(2026, 1, 1),
                        LocalDate.of(2028, 12, 31),
                        3
                )
        ));

        mockMvc.perform(get("/api/projects"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("디지털 보험 플랫폼"))
                .andExpect(jsonPath("$[0].cashFlowCount").value(3))
                .andExpect(jsonPath("$[0].description").doesNotExist());
    }

    @Test
    void findOne_shouldReturnProjectDetailResponseWithCashFlows() throws Exception {
        when(service.findDetailById(1L)).thenReturn(new ProjectDetailResponse(
                1L,
                "디지털 보험 플랫폼",
                "보험",
                BigDecimal.valueOf(100000000),
                8.5,
                ProjectStatus.REVIEW,
                "전략기획팀",
                LocalDate.of(2026, 1, 1),
                LocalDate.of(2028, 12, 31),
                "보험 채널 디지털 전환 프로젝트",
                LocalDateTime.of(2026, 4, 23, 9, 0),
                List.of(new ProjectCashFlowResponse(1, BigDecimal.valueOf(35000000), BigDecimal.valueOf(5000000), BigDecimal.valueOf(30000000)))
        ));

        mockMvc.perform(get("/api/projects/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value("보험 채널 디지털 전환 프로젝트"))
                .andExpect(jsonPath("$.cashFlows[0].periodYear").value(1))
                .andExpect(jsonPath("$.cashFlows[0].netCashFlow").value(30000000));
    }

    @Test
    void history_shouldReturnSnapshotResponseWithoutLazyProjectGraph() throws Exception {
        when(service.findRecentSnapshots(1L)).thenReturn(List.of(
                new ProjectEvaluationSnapshotResponse(
                        10L,
                        1L,
                        "디지털 보험 플랫폼",
                        12500000.12,
                        13.4,
                        2.8,
                        34.2,
                        9900000.0,
                        ProjectEvaluationGrade.EXCELLENT,
                        LocalDateTime.of(2026, 4, 23, 9, 10)
                )
        ));

        mockMvc.perform(get("/api/projects/1/history"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].projectId").value(1))
                .andExpect(jsonPath("$[0].projectName").value("디지털 보험 플랫폼"))
                .andExpect(jsonPath("$[0].grade").value("EXCELLENT"));
    }
}
