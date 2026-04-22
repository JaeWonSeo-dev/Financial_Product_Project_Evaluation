package com.portfolio.finance.domain;

public enum ProductType {
    BOND("채권"),
    LOAN("대출"),
    FUND("펀드"),
    DERIVATIVE("파생상품"),
    EQUITY("주식형"),
    HYBRID("혼합형");

    private final String label;

    ProductType(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
