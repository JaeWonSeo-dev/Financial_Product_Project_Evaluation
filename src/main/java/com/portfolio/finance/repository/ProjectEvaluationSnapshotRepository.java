package com.portfolio.finance.repository;

import com.portfolio.finance.domain.ProjectEvaluationSnapshot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectEvaluationSnapshotRepository extends JpaRepository<ProjectEvaluationSnapshot, Long> {
    List<ProjectEvaluationSnapshot> findTop10ByProjectIdOrderByEvaluatedAtDesc(Long projectId);
}
