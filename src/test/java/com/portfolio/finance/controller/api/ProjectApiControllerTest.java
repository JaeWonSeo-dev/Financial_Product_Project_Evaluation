package com.portfolio.finance.controller.api;

import com.portfolio.finance.dto.CashFlowInput;
import com.portfolio.finance.dto.ProjectRequest;
import com.portfolio.finance.domain.ProjectStatus;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

class ProjectApiControllerTest {

    private static ValidatorFactory validatorFactory;
    private static Validator validator;

    @BeforeAll
    static void setUp() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @AfterAll
    static void tearDown() {
        validatorFactory.close();
    }

    @Test
    void projectRequest_shouldFailValidation_whenEndDateBeforeStartDate() {
        ProjectRequest request = validRequest();
        request.setEndDate(request.getStartDate().minusDays(1));

        var violations = validator.validate(request);

        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().equals("프로젝트 종료일은 시작일보다 빠를 수 없습니다.")));
    }

    @Test
    void projectRequest_shouldFailValidation_whenCashFlowYearsDuplicated() {
        ProjectRequest request = validRequest();
        request.setCashFlows(List.of(
                cashFlow(1, 2000, 1000),
                cashFlow(1, 2500, 1200)
        ));

        var violations = validator.validate(request);

        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().equals("현금흐름 연차는 중복될 수 없습니다.")));
    }

    @Test
    void projectRequest_shouldFailValidation_whenCashFlowsMissing() {
        ProjectRequest request = validRequest();
        request.setCashFlows(List.of());

        var violations = validator.validate(request);

        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().equals("최소 1건 이상의 현금흐름이 필요합니다.")));
    }

    private ProjectRequest validRequest() {
        ProjectRequest request = new ProjectRequest();
        request.setName("신규 디지털 보험 플랫폼");
        request.setCategory("보험");
        request.setInitialInvestment(BigDecimal.valueOf(100000000));
        request.setDiscountRate(8.5);
        request.setStatus(ProjectStatus.REVIEW);
        request.setOwner("전략기획팀");
        request.setStartDate(LocalDate.of(2026, 1, 1));
        request.setEndDate(LocalDate.of(2028, 12, 31));
        request.setDescription("보험 채널 디지털 전환 프로젝트");
        request.setCashFlows(List.of(
                cashFlow(1, 35000000, 5000000),
                cashFlow(2, 40000000, 4000000)
        ));
        return request;
    }

    private CashFlowInput cashFlow(int year, int inflow, int outflow) {
        CashFlowInput input = new CashFlowInput();
        input.setPeriodYear(year);
        input.setInflow(BigDecimal.valueOf(inflow));
        input.setOutflow(BigDecimal.valueOf(outflow));
        return input;
    }
}
