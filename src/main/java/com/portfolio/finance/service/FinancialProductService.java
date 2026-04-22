package com.portfolio.finance.service;

import com.portfolio.finance.domain.FinancialProduct;
import com.portfolio.finance.dto.ProductRequest;
import com.portfolio.finance.repository.FinancialProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FinancialProductService {
    private final FinancialProductRepository repository;
    public FinancialProductService(FinancialProductRepository repository) { this.repository = repository; }
    public List<FinancialProduct> findAll() { return repository.findAll(); }
    public FinancialProduct findById(Long id) { return repository.findById(id).orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다. id=" + id)); }
    public FinancialProduct create(ProductRequest request) { return repository.save(toEntity(new FinancialProduct(), request)); }
    public FinancialProduct update(Long id, ProductRequest request) { return repository.save(toEntity(findById(id), request)); }
    public void delete(Long id) { repository.delete(findById(id)); }
    private FinancialProduct toEntity(FinancialProduct product, ProductRequest request) {
        product.setName(request.getName()); product.setProductType(request.getProductType()); product.setInvestmentAmount(request.getInvestmentAmount());
        product.setExpectedReturnRate(request.getExpectedReturnRate()); product.setDiscountRate(request.getDiscountRate()); product.setMaturityMonths(request.getMaturityMonths());
        product.setRiskGrade(request.getRiskGrade()); product.setAnnualVolatility(request.getAnnualVolatility()); product.setIssuer(request.getIssuer());
        product.setDescription(request.getDescription()); return product;
    }
}
