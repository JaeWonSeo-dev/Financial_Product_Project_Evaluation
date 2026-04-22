package com.portfolio.finance.controller.api;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.core.MethodParameter;

import java.lang.reflect.Method;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ApiExceptionHandlerTest {

    private final ApiExceptionHandler handler = new ApiExceptionHandler();

    @Test
    void handleIllegalArgument_shouldReturnNotFoundPayload() {
        Map<String, Object> response = handler.handleIllegalArgument(new IllegalArgumentException("프로젝트를 찾을 수 없습니다. id=99"));

        assertEquals(HttpStatus.NOT_FOUND.value(), response.get("status"));
        assertEquals("RESOURCE_NOT_FOUND", response.get("error"));
        assertEquals("프로젝트를 찾을 수 없습니다. id=99", response.get("message"));
    }

    @Test
    void handleValidation_shouldReturnFieldErrors() throws Exception {
        BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(new DummyRequest(), "projectRequest");
        bindingResult.addError(new FieldError("projectRequest", "cashFlows", null, false, null, null, "최소 1건 이상의 현금흐름이 필요합니다."));

        Method method = DummyController.class.getDeclaredMethod("create", DummyRequest.class);
        MethodArgumentNotValidException ex = new MethodArgumentNotValidException(new MethodParameter(method, 0), bindingResult);

        Map<String, Object> response = handler.handleValidation(ex);
        Map<String, String> fieldErrors = (Map<String, String>) response.get("fieldErrors");

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.get("status"));
        assertEquals("VALIDATION_FAILED", response.get("error"));
        assertEquals("최소 1건 이상의 현금흐름이 필요합니다.", fieldErrors.get("cashFlows"));
    }

    static class DummyController {
        public void create(DummyRequest request) {
        }
    }

    static class DummyRequest {
    }
}
