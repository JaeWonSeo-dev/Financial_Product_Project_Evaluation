package com.portfolio.finance.domain;

public enum ProjectStatus {
    REVIEW("검토중"),
    APPROVED("승인"),
    HOLD("보류");

    private final String label;

    ProjectStatus(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
