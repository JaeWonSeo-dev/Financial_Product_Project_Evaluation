package com.portfolio.finance.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class EvaluationProject {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String category;
    @Column(precision = 18, scale = 2)
    private BigDecimal initialInvestment;
    private double discountRate;
    @Enumerated(EnumType.STRING)
    private ProjectStatus status;
    private String owner;
    private LocalDate startDate;
    private LocalDate endDate;
    @Column(length = 2000)
    private String description;
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("periodYear ASC")
    private List<ProjectCashFlow> cashFlows = new ArrayList<>();

    public EvaluationProject() {}

    public EvaluationProject(String name, String category, BigDecimal initialInvestment, double discountRate,
                             ProjectStatus status, String owner, LocalDate startDate, LocalDate endDate, String description) {
        this.name = name;
        this.category = category;
        this.initialInvestment = initialInvestment;
        this.discountRate = discountRate;
        this.status = status;
        this.owner = owner;
        this.startDate = startDate;
        this.endDate = endDate;
        this.description = description;
        this.createdAt = LocalDateTime.now();
    }

    @PrePersist
    public void prePersist() { if (createdAt == null) createdAt = LocalDateTime.now(); }
    public void addCashFlow(ProjectCashFlow cashFlow) { cashFlows.add(cashFlow); cashFlow.setProject(this); }
    public void clearCashFlows() { for (ProjectCashFlow cashFlow : cashFlows) { cashFlow.setProject(null); } cashFlows.clear(); }
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public BigDecimal getInitialInvestment() { return initialInvestment; }
    public void setInitialInvestment(BigDecimal initialInvestment) { this.initialInvestment = initialInvestment; }
    public double getDiscountRate() { return discountRate; }
    public void setDiscountRate(double discountRate) { this.discountRate = discountRate; }
    public ProjectStatus getStatus() { return status; }
    public void setStatus(ProjectStatus status) { this.status = status; }
    public String getOwner() { return owner; }
    public void setOwner(String owner) { this.owner = owner; }
    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }
    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public List<ProjectCashFlow> getCashFlows() { return cashFlows; }
    public void setCashFlows(List<ProjectCashFlow> cashFlows) { this.cashFlows = cashFlows; }
}
