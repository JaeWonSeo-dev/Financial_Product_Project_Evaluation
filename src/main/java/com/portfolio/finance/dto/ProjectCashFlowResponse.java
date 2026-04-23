package com.portfolio.finance.dto;

import java.math.BigDecimal;

public record ProjectCashFlowResponse(
        int periodYear,
        BigDecimal inflow,
        BigDecimal outflow,
        BigDecimal netCashFlow
) {
}
