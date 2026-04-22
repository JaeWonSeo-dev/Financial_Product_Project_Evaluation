package com.portfolio.finance.dto;

import com.portfolio.finance.domain.ProjectStatus;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ProjectRequest {
    @NotBlank private String name;
    @NotBlank private String category;
    @NotNull @DecimalMin("0.0") private BigDecimal initialInvestment;
    @DecimalMin("0.0") @DecimalMax("100.0") private double discountRate;
    @NotNull private ProjectStatus status;
    @NotBlank private String owner;
    @NotNull private LocalDate startDate;
    @NotNull private LocalDate endDate;
    @Size(max = 2000) private String description;
    @NotEmpty(message = "최소 1건 이상의 현금흐름이 필요합니다.")
    @Valid private List<CashFlowInput> cashFlows = new ArrayList<>();
    public String getName() { return name; } public void setName(String name) { this.name = name; }
    public String getCategory() { return category; } public void setCategory(String category) { this.category = category; }
    public BigDecimal getInitialInvestment() { return initialInvestment; } public void setInitialInvestment(BigDecimal initialInvestment) { this.initialInvestment = initialInvestment; }
    public double getDiscountRate() { return discountRate; } public void setDiscountRate(double discountRate) { this.discountRate = discountRate; }
    public ProjectStatus getStatus() { return status; } public void setStatus(ProjectStatus status) { this.status = status; }
    public String getOwner() { return owner; } public void setOwner(String owner) { this.owner = owner; }
    public LocalDate getStartDate() { return startDate; } public void setStartDate(LocalDate startDate) { this.startDate = startDate; }
    public LocalDate getEndDate() { return endDate; } public void setEndDate(LocalDate endDate) { this.endDate = endDate; }
    public String getDescription() { return description; } public void setDescription(String description) { this.description = description; }
    public List<CashFlowInput> getCashFlows() { return cashFlows; } public void setCashFlows(List<CashFlowInput> cashFlows) { this.cashFlows = cashFlows; }

    @AssertTrue(message = "프로젝트 종료일은 시작일보다 빠를 수 없습니다.")
    public boolean isDateRangeValid() {
        return startDate == null || endDate == null || !endDate.isBefore(startDate);
    }

    @AssertTrue(message = "현금흐름 연차는 중복될 수 없습니다.")
    public boolean isCashFlowYearUnique() {
        if (cashFlows == null) {
            return true;
        }

        Set<Integer> years = new HashSet<>();
        return cashFlows.stream()
                .map(CashFlowInput::getPeriodYear)
                .allMatch(years::add);
    }
}
