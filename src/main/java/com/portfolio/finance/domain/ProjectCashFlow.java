package com.portfolio.finance.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
public class ProjectCashFlow {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int periodYear;
    @Column(precision = 18, scale = 2)
    private BigDecimal inflow;
    @Column(precision = 18, scale = 2)
    private BigDecimal outflow;
    @ManyToOne(fetch = FetchType.LAZY)
    private EvaluationProject project;

    public ProjectCashFlow() {}
    public ProjectCashFlow(int periodYear, BigDecimal inflow, BigDecimal outflow) {
        this.periodYear = periodYear; this.inflow = inflow; this.outflow = outflow;
    }
    public BigDecimal netCashFlow() { return inflow.subtract(outflow); }
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public int getPeriodYear() { return periodYear; }
    public void setPeriodYear(int periodYear) { this.periodYear = periodYear; }
    public BigDecimal getInflow() { return inflow; }
    public void setInflow(BigDecimal inflow) { this.inflow = inflow; }
    public BigDecimal getOutflow() { return outflow; }
    public void setOutflow(BigDecimal outflow) { this.outflow = outflow; }
    public EvaluationProject getProject() { return project; }
    public void setProject(EvaluationProject project) { this.project = project; }
}
