package com.portfolio.finance.service;

import com.portfolio.finance.domain.EvaluationProject;
import com.portfolio.finance.domain.ProjectCashFlow;
import com.portfolio.finance.domain.ProjectEvaluationSnapshot;
import com.portfolio.finance.domain.ProjectStatus;
import com.portfolio.finance.repository.EvaluationProjectRepository;
import com.portfolio.finance.repository.ProjectEvaluationSnapshotRepository;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ProjectEvaluationServiceTest {

    private final EvaluationProjectRepository repository = mock(EvaluationProjectRepository.class);
    private final ProjectEvaluationSnapshotRepository snapshotRepository = mock(ProjectEvaluationSnapshotRepository.class);
    private final FinancialCalculationEngine calculationEngine = new FinancialCalculationEngine();
    private final ProjectEvaluationService service = new ProjectEvaluationService(repository, snapshotRepository, calculationEngine);

    @Test
    void evaluate_shouldPersistSnapshotWithCalculatedValues() {
        EvaluationProject project = sampleProject();
        when(repository.findWithCashFlowsById(1L)).thenReturn(Optional.of(project));

        var response = service.evaluate(1L);

        ArgumentCaptor<ProjectEvaluationSnapshot> captor = ArgumentCaptor.forClass(ProjectEvaluationSnapshot.class);
        verify(snapshotRepository, times(1)).save(captor.capture());
        ProjectEvaluationSnapshot snapshot = captor.getValue();

        assertThat(snapshot.getProject()).isEqualTo(project);
        assertThat(snapshot.getNpv()).isEqualTo(response.npv());
        assertThat(snapshot.getIrr()).isEqualTo(response.irr());
        assertThat(snapshot.getRiskScore()).isEqualTo(response.riskScore());
        assertThat(snapshot.getGrade()).isEqualTo(response.grade());
        assertThat(snapshot.getEvaluatedAt()).isNotNull();
    }

    @Test
    void findRecentSnapshots_shouldReturnRepositoryHistory() {
        EvaluationProject project = sampleProject();
        ProjectEvaluationSnapshot snapshot = new ProjectEvaluationSnapshot(project, 120.0, 18.0, 2.5, 32.0, 10.0, calculationEngine.grade(120.0, 18.0, 32.0));
        when(repository.findWithCashFlowsById(1L)).thenReturn(Optional.of(project));
        when(snapshotRepository.findTop10ByProjectIdOrderByEvaluatedAtDesc(1L)).thenReturn(List.of(snapshot));

        List<ProjectEvaluationSnapshot> result = service.findRecentSnapshots(1L);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getNpv()).isEqualTo(120.0);
    }

    private EvaluationProject sampleProject() {
        EvaluationProject project = new EvaluationProject();
        project.setId(1L);
        project.setName("신규 디지털 보험 플랫폼");
        project.setCategory("보험");
        project.setInitialInvestment(BigDecimal.valueOf(1000));
        project.setDiscountRate(10.0);
        project.setStatus(ProjectStatus.REVIEW);
        project.setOwner("전략기획팀");
        project.setStartDate(LocalDate.of(2026, 1, 1));
        project.setEndDate(LocalDate.of(2028, 12, 31));
        project.addCashFlow(new ProjectCashFlow(1, BigDecimal.valueOf(500), BigDecimal.valueOf(100)));
        project.addCashFlow(new ProjectCashFlow(2, BigDecimal.valueOf(550), BigDecimal.valueOf(100)));
        project.addCashFlow(new ProjectCashFlow(3, BigDecimal.valueOf(600), BigDecimal.valueOf(100)));
        return project;
    }
}
