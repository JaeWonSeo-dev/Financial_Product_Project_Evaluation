package com.portfolio.finance.service;

import com.portfolio.finance.domain.EvaluationProject;
import com.portfolio.finance.domain.ProjectCashFlow;
import com.portfolio.finance.domain.ProjectEvaluationGrade;
import com.portfolio.finance.domain.ProjectEvaluationSnapshot;
import com.portfolio.finance.dto.ProjectCashFlowResponse;
import com.portfolio.finance.dto.ProjectDetailResponse;
import com.portfolio.finance.dto.ProjectEvaluationResponse;
import com.portfolio.finance.dto.ProjectEvaluationSnapshotResponse;
import com.portfolio.finance.dto.ProjectRequest;
import com.portfolio.finance.dto.ProjectSummaryResponse;
import com.portfolio.finance.repository.EvaluationProjectRepository;
import com.portfolio.finance.repository.ProjectEvaluationSnapshotRepository;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class ProjectEvaluationService {
    private final EvaluationProjectRepository repository;
    private final ProjectEvaluationSnapshotRepository snapshotRepository;
    private final FinancialCalculationEngine calculationEngine;

    public ProjectEvaluationService(EvaluationProjectRepository repository,
                                    ProjectEvaluationSnapshotRepository snapshotRepository,
                                    FinancialCalculationEngine calculationEngine) {
        this.repository = repository;
        this.snapshotRepository = snapshotRepository;
        this.calculationEngine = calculationEngine;
    }

    public List<EvaluationProject> findAll() {
        return repository.findAll().stream()
                .sorted(Comparator.comparing(EvaluationProject::getCreatedAt).reversed())
                .toList();
    }

    public List<ProjectSummaryResponse> findAllSummaries() {
        return findAll().stream().map(this::toSummaryResponse).toList();
    }

    public EvaluationProject findById(Long id) {
        return repository.findWithCashFlowsById(id)
                .orElseThrow(() -> new IllegalArgumentException("프로젝트를 찾을 수 없습니다. id=" + id));
    }

    public ProjectDetailResponse findDetailById(Long id) {
        return toDetailResponse(findById(id));
    }

    public ProjectDetailResponse create(ProjectRequest request) {
        return toDetailResponse(repository.save(toEntity(new EvaluationProject(), request)));
    }

    public ProjectDetailResponse update(Long id, ProjectRequest request) {
        return toDetailResponse(repository.save(toEntity(findById(id), request)));
    }

    public void delete(Long id) {
        repository.delete(findById(id));
    }

    public ProjectEvaluationResponse evaluate(Long projectId) {
        EvaluationProject project = findById(projectId);
        List<Double> netCashFlows = project.getCashFlows().stream()
                .sorted(Comparator.comparing(ProjectCashFlow::getPeriodYear))
                .map(cf -> cf.netCashFlow().doubleValue())
                .toList();
        double initialInvestment = project.getInitialInvestment().doubleValue();
        double npv = calculationEngine.calculateNpv(initialInvestment, netCashFlows, project.getDiscountRate());
        double irr = calculationEngine.calculateIrr(initialInvestment, netCashFlows);
        double payback = calculationEngine.calculatePaybackPeriod(initialInvestment, netCashFlows);
        double avgVolatility = netCashFlows.isEmpty()
                ? 0.0
                : netCashFlows.stream().mapToDouble(Math::abs).average().orElse(0.0) / Math.max(initialInvestment, 1) * 100.0;
        double riskScore = calculationEngine.calculateRiskScore(project.getDiscountRate(), irr, avgVolatility, payback < 0 ? 5 : payback);
        double varEstimate = calculationEngine.estimateVarStyleLoss(initialInvestment, avgVolatility);
        ProjectEvaluationGrade grade = calculationEngine.grade(npv, irr, riskScore);

        snapshotRepository.save(new ProjectEvaluationSnapshot(project, npv, irr, payback, riskScore, varEstimate, grade));

        return new ProjectEvaluationResponse(project.getId(), project.getName(), npv, irr, payback, riskScore, varEstimate,
                grade,
                calculationEngine.scenarioAnalysis(initialInvestment, netCashFlows, project.getDiscountRate()),
                calculationEngine.sensitivityAnalysis(initialInvestment, netCashFlows, project.getDiscountRate()),
                netCashFlows);
    }

    public List<ProjectEvaluationSnapshotResponse> findRecentSnapshots(Long projectId) {
        findById(projectId);
        return snapshotRepository.findTop10ByProjectIdOrderByEvaluatedAtDesc(projectId).stream()
                .map(this::toSnapshotResponse)
                .toList();
    }

    public ProjectSummaryResponse toSummaryResponse(EvaluationProject project) {
        return new ProjectSummaryResponse(
                project.getId(),
                project.getName(),
                project.getCategory(),
                project.getInitialInvestment(),
                project.getDiscountRate(),
                project.getStatus(),
                project.getOwner(),
                project.getStartDate(),
                project.getEndDate(),
                project.getCashFlows().size()
        );
    }

    public ProjectDetailResponse toDetailResponse(EvaluationProject project) {
        return new ProjectDetailResponse(
                project.getId(),
                project.getName(),
                project.getCategory(),
                project.getInitialInvestment(),
                project.getDiscountRate(),
                project.getStatus(),
                project.getOwner(),
                project.getStartDate(),
                project.getEndDate(),
                project.getDescription(),
                project.getCreatedAt(),
                project.getCashFlows().stream()
                        .sorted(Comparator.comparing(ProjectCashFlow::getPeriodYear))
                        .map(cf -> new ProjectCashFlowResponse(cf.getPeriodYear(), cf.getInflow(), cf.getOutflow(), cf.netCashFlow()))
                        .toList()
        );
    }

    private ProjectEvaluationSnapshotResponse toSnapshotResponse(ProjectEvaluationSnapshot snapshot) {
        return new ProjectEvaluationSnapshotResponse(
                snapshot.getId(),
                snapshot.getProject().getId(),
                snapshot.getProject().getName(),
                snapshot.getNpv(),
                snapshot.getIrr(),
                snapshot.getPaybackPeriod(),
                snapshot.getRiskScore(),
                snapshot.getVarEstimate(),
                snapshot.getGrade(),
                snapshot.getEvaluatedAt()
        );
    }

    private EvaluationProject toEntity(EvaluationProject project, ProjectRequest request) {
        project.setName(request.getName());
        project.setCategory(request.getCategory());
        project.setInitialInvestment(request.getInitialInvestment());
        project.setDiscountRate(request.getDiscountRate());
        project.setStatus(request.getStatus());
        project.setOwner(request.getOwner());
        project.setStartDate(request.getStartDate());
        project.setEndDate(request.getEndDate());
        project.setDescription(request.getDescription());
        project.clearCashFlows();
        request.getCashFlows().stream()
                .filter(cf -> cf.getInflow() != null && cf.getOutflow() != null)
                .forEach(cf -> project.addCashFlow(new ProjectCashFlow(cf.getPeriodYear(), cf.getInflow(), cf.getOutflow())));
        return project;
    }
}
