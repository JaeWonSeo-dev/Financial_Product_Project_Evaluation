package com.portfolio.finance.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
public class FinancialProduct {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Enumerated(EnumType.STRING)
    private ProductType productType;
    @Column(precision = 18, scale = 2)
    private BigDecimal investmentAmount;
    private double expectedReturnRate;
    private double discountRate;
    private int maturityMonths;
    @Enumerated(EnumType.STRING)
    private RiskGrade riskGrade;
    private double annualVolatility;
    private String issuer;
    @Column(length = 2000)
    private String description;
    private LocalDateTime createdAt;

    public FinancialProduct() {}

    public FinancialProduct(String name, ProductType productType, BigDecimal investmentAmount, double expectedReturnRate,
                            double discountRate, int maturityMonths, RiskGrade riskGrade, double annualVolatility,
                            String issuer, String description) {
        this.name = name;
        this.productType = productType;
        this.investmentAmount = investmentAmount;
        this.expectedReturnRate = expectedReturnRate;
        this.discountRate = discountRate;
        this.maturityMonths = maturityMonths;
        this.riskGrade = riskGrade;
        this.annualVolatility = annualVolatility;
        this.issuer = issuer;
        this.description = description;
        this.createdAt = LocalDateTime.now();
    }

    @PrePersist
    public void prePersist() { if (createdAt == null) createdAt = LocalDateTime.now(); }
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public ProductType getProductType() { return productType; }
    public void setProductType(ProductType productType) { this.productType = productType; }
    public BigDecimal getInvestmentAmount() { return investmentAmount; }
    public void setInvestmentAmount(BigDecimal investmentAmount) { this.investmentAmount = investmentAmount; }
    public double getExpectedReturnRate() { return expectedReturnRate; }
    public void setExpectedReturnRate(double expectedReturnRate) { this.expectedReturnRate = expectedReturnRate; }
    public double getDiscountRate() { return discountRate; }
    public void setDiscountRate(double discountRate) { this.discountRate = discountRate; }
    public int getMaturityMonths() { return maturityMonths; }
    public void setMaturityMonths(int maturityMonths) { this.maturityMonths = maturityMonths; }
    public RiskGrade getRiskGrade() { return riskGrade; }
    public void setRiskGrade(RiskGrade riskGrade) { this.riskGrade = riskGrade; }
    public double getAnnualVolatility() { return annualVolatility; }
    public void setAnnualVolatility(double annualVolatility) { this.annualVolatility = annualVolatility; }
    public String getIssuer() { return issuer; }
    public void setIssuer(String issuer) { this.issuer = issuer; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
