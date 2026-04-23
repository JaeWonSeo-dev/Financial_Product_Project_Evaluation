package com.portfolio.finance;

import com.portfolio.finance.repository.EvaluationProjectRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@SpringBootTest
class FinancialProductProjectEvaluationApplicationTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private EvaluationProjectRepository projectRepository;

    @BeforeEach
    void setUp() {
        mockMvc = webAppContextSetup(webApplicationContext).build();
    }

    @Test
    void dashboardAndProjectHistoryFlow_shouldWorkWithSampleData() throws Exception {
        Long projectId = projectRepository.findAll().stream()
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("샘플 프로젝트가 없습니다."))
                .getId();

        mockMvc.perform(get("/api/dashboard"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalProducts").value(greaterThanOrEqualTo(4)))
                .andExpect(jsonPath("$.totalProjects").value(greaterThanOrEqualTo(2)))
                .andExpect(jsonPath("$.projectComparisons").isArray());

        mockMvc.perform(get("/api/projects/{id}/evaluation", projectId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.projectId").value(projectId))
                .andExpect(jsonPath("$.projectName").isNotEmpty())
                .andExpect(jsonPath("$.scenarios").isArray())
                .andExpect(jsonPath("$.sensitivities").isArray());

        mockMvc.perform(get("/api/projects/{id}/history", projectId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].projectId").value(projectId))
                .andExpect(jsonPath("$[0].evaluatedAt").isNotEmpty())
                .andExpect(jsonPath("$[0].grade").isNotEmpty());
    }
}
