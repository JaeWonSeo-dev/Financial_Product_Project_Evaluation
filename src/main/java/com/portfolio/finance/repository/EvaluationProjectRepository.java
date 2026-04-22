package com.portfolio.finance.repository;

import com.portfolio.finance.domain.EvaluationProject;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EvaluationProjectRepository extends JpaRepository<EvaluationProject, Long> {
    @Override
    @EntityGraph(attributePaths = "cashFlows")
    List<EvaluationProject> findAll();

    @EntityGraph(attributePaths = "cashFlows")
    Optional<EvaluationProject> findWithCashFlowsById(Long id);
}
