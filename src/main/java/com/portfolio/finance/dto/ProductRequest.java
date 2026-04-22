package com.portfolio.finance.dto;

import com.portfolio.finance.domain.ProductType;
import com.portfolio.finance.domain.RiskGrade;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public class ProductRequest {
    @NotBlank private String name;
    @NotNull private ProductType productType;
    @NotNull @DecimalMin("0.0") private BigDecimal investmentAmount;
    @DecimalMin("0.0") @DecimalMax("100.0") private double expectedReturnRate;
    @DecimalMin("0.0") @DecimalMax("100.0") private double discountRate;
    @Min(1) private int maturityMonths;
    @NotNull private RiskGrade riskGrade;
    @DecimalMin("0.0") @DecimalMax("100.0") private double annualVolatility;
    @NotBlank private String issuer;
    @Size(max = 2000) private String description;
    public String getName() { return name; } public void setName(String name) { this.name = name; }
    public ProductType getProductType() { return productType; } public void setProductType(ProductType productType) { this.productType = productType; }
    public BigDecimal getInvestmentAmount() { return investmentAmount; } public void setInvestmentAmount(BigDecimal investmentAmount) { this.investmentAmount = investmentAmount; }
    public double getExpectedReturnRate() { return expectedReturnRate; } public void setExpectedReturnRate(double expectedReturnRate) { this.expectedReturnRate = expectedReturnRate; }
    public double getDiscountRate() { return discountRate; } public void setDiscountRate(double discountRate) { this.discountRate = discountRate; }
    public int getMaturityMonths() { return maturityMonths; } public void setMaturityMonths(int maturityMonths) { this.maturityMonths = maturityMonths; }
    public RiskGrade getRiskGrade() { return riskGrade; } public void setRiskGrade(RiskGrade riskGrade) { this.riskGrade = riskGrade; }
    public double getAnnualVolatility() { return annualVolatility; } public void setAnnualVolatility(double annualVolatility) { this.annualVolatility = annualVolatility; }
    public String getIssuer() { return issuer; } public void setIssuer(String issuer) { this.issuer = issuer; }
    public String getDescription() { return description; } public void setDescription(String description) { this.description = description; }
}
