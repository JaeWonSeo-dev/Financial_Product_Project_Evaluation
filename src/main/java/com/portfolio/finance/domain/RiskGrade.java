package com.portfolio.finance.domain;

public enum RiskGrade {
    LOW("낮음", 1),
    MEDIUM("보통", 2),
    HIGH("높음", 3),
    VERY_HIGH("매우높음", 4);

    private final String label;
    private final int score;

    RiskGrade(String label, int score) {
        this.label = label;
        this.score = score;
    }

    public String getLabel() {
        return label;
    }

    public int getScore() {
        return score;
    }
}
