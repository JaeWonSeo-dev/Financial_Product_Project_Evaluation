package com.portfolio.finance.controller.api;

import com.portfolio.finance.domain.FinancialProduct;
import com.portfolio.finance.dto.ProductRequest;
import com.portfolio.finance.service.FinancialProductService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductApiController {
    private final FinancialProductService service;
    public ProductApiController(FinancialProductService service) { this.service = service; }
    @GetMapping public List<FinancialProduct> findAll() { return service.findAll(); }
    @GetMapping("/{id}") public FinancialProduct findOne(@PathVariable Long id) { return service.findById(id); }
    @PostMapping public FinancialProduct create(@Valid @RequestBody ProductRequest request) { return service.create(request); }
    @PutMapping("/{id}") public FinancialProduct update(@PathVariable Long id, @Valid @RequestBody ProductRequest request) { return service.update(id, request); }
    @DeleteMapping("/{id}") public void delete(@PathVariable Long id) { service.delete(id); }
}
