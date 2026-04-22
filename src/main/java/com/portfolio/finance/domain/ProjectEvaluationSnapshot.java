package com.portfolio.finance.domain;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class ProjectEvaluationSnapshot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private EvaluationProject project;

    private double npv;
    private double irr;
    private double paybackPeriod;
    private double riskScore;
    private double varEstimate;

    @Enumerated(EnumType.STRING)
    private ProjectEvaluationGrade grade;

    private LocalDateTime evaluatedAt;

    public ProjectEvaluationSnapshot() {
    }

    public ProjectEvaluationSnapshot(EvaluationProject project, double npv, double irr, double paybackPeriod,
                                     double riskScore, double varEstimate, ProjectEvaluationGrade grade) {
        this.project = project;
        this.npv = npv;
        this.irr = irr;
        this.paybackPeriod = paybackPeriod;
        this.riskScore = riskScore;
        this.varEstimate = varEstimate;
        this.grade = grade;
        this.evaluatedAt = LocalDateTime.now();
    }

    @PrePersist
    public void prePersist() {
        if (evaluatedAt == null) {
            evaluatedAt = LocalDateTime.now();
        }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public EvaluationProject getProject() { return project; }
    public void setProject(EvaluationProject project) { this.project = project; }
    public double getNpv() { return npv; }
    public void setNpv(double npv) { this.npv = npv; }
    public double getIrr() { return irr; }
    public void setIrr(double irr) { this.irr = irr; }
    public double getPaybackPeriod() { return paybackPeriod; }
    public void setPaybackPeriod(double paybackPeriod) { this.paybackPeriod = paybackPeriod; }
    public double getRiskScore() { return riskScore; }
    public void setRiskScore(double riskScore) { this.riskScore = riskScore; }
    public double getVarEstimate() { return varEstimate; }
    public void setVarEstimate(double varEstimate) { this.varEstimate = varEstimate; }
    public ProjectEvaluationGrade getGrade() { return grade; }
    public void setGrade(ProjectEvaluationGrade grade) { this.grade = grade; }
    public LocalDateTime getEvaluatedAt() { return evaluatedAt; }
    public void setEvaluatedAt(LocalDateTime evaluatedAt) { this.evaluatedAt = evaluatedAt; }
}
