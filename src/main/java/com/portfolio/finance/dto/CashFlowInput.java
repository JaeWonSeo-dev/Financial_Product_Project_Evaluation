package com.portfolio.finance.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public class CashFlowInput {
    @Min(1) private int periodYear;
    @NotNull @DecimalMin("0.0") private BigDecimal inflow;
    @NotNull @DecimalMin("0.0") private BigDecimal outflow;
    public int getPeriodYear() { return periodYear; } public void setPeriodYear(int periodYear) { this.periodYear = periodYear; }
    public BigDecimal getInflow() { return inflow; } public void setInflow(BigDecimal inflow) { this.inflow = inflow; }
    public BigDecimal getOutflow() { return outflow; } public void setOutflow(BigDecimal outflow) { this.outflow = outflow; }
}
