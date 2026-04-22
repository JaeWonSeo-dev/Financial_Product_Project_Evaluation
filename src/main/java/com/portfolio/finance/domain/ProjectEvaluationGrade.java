package com.portfolio.finance.domain;

public enum ProjectEvaluationGrade {
    EXCELLENT("우수"),
    NORMAL("보통"),
    RISKY("위험");

    private final String label;

    ProjectEvaluationGrade(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
