package com.portfolio.finance.config;

import com.portfolio.finance.domain.*;
import com.portfolio.finance.repository.EvaluationProjectRepository;
import com.portfolio.finance.repository.FinancialProductRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;

@Component
public class SampleDataLoader {
    private final FinancialProductRepository productRepository;
    private final EvaluationProjectRepository projectRepository;
    public SampleDataLoader(FinancialProductRepository productRepository, EvaluationProjectRepository projectRepository) {
        this.productRepository = productRepository; this.projectRepository = projectRepository;
    }
    @PostConstruct
    void load() {
        if (productRepository.count() > 0 || projectRepository.count() > 0) return;
        productRepository.save(new FinancialProduct("국공채 안정형 포트폴리오", ProductType.BOND, new BigDecimal("500000000"), 4.2, 3.0, 36, RiskGrade.LOW, 6.5, "Korea Treasury", "장기 안정 수익 확보용 채권형 상품"));
        productRepository.save(new FinancialProduct("중금리 기업대출 패키지", ProductType.LOAN, new BigDecimal("350000000"), 7.8, 5.2, 24, RiskGrade.MEDIUM, 11.5, "Corporate Lending Desk", "중소기업 여신 포트폴리오"));
        productRepository.save(new FinancialProduct("신재생 인프라 펀드", ProductType.FUND, new BigDecimal("800000000"), 11.4, 7.1, 60, RiskGrade.HIGH, 18.5, "Green Capital", "신재생 발전소 지분 투자형 펀드"));
        productRepository.save(new FinancialProduct("환헤지 파생전략", ProductType.DERIVATIVE, new BigDecimal("220000000"), 13.2, 8.0, 12, RiskGrade.VERY_HIGH, 25.0, "Derivatives Lab", "환율 및 금리 변동 대응형 구조화 전략"));

        EvaluationProject claimAi = new EvaluationProject("AI 보험금 자동심사 도입", "디지털 혁신", new BigDecimal("1200000000"), 8.5, ProjectStatus.REVIEW, "디지털전략팀", LocalDate.now().minusMonths(1), LocalDate.now().plusYears(4), "보험금 자동심사 엔진 도입 프로젝트");
        claimAi.addCashFlow(new ProjectCashFlow(1, new BigDecimal("320000000"), new BigDecimal("40000000")));
        claimAi.addCashFlow(new ProjectCashFlow(2, new BigDecimal("420000000"), new BigDecimal("50000000")));
        claimAi.addCashFlow(new ProjectCashFlow(3, new BigDecimal("520000000"), new BigDecimal("60000000")));
        claimAi.addCashFlow(new ProjectCashFlow(4, new BigDecimal("560000000"), new BigDecimal("70000000")));
        projectRepository.save(claimAi);

        EvaluationProject crm = new EvaluationProject("차세대 고객플랫폼 구축", "플랫폼 전환", new BigDecimal("900000000"), 9.0, ProjectStatus.APPROVED, "고객플랫폼팀", LocalDate.now().minusMonths(2), LocalDate.now().plusYears(3), "옴니채널 고객 경험 개선 프로젝트");
        crm.addCashFlow(new ProjectCashFlow(1, new BigDecimal("220000000"), new BigDecimal("30000000")));
        crm.addCashFlow(new ProjectCashFlow(2, new BigDecimal("280000000"), new BigDecimal("35000000")));
        crm.addCashFlow(new ProjectCashFlow(3, new BigDecimal("390000000"), new BigDecimal("45000000")));
        crm.addCashFlow(new ProjectCashFlow(4, new BigDecimal("410000000"), new BigDecimal("50000000")));
        projectRepository.save(crm);
    }
}
