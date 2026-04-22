package com.portfolio.finance.repository;

import com.portfolio.finance.domain.FinancialProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FinancialProductRepository extends JpaRepository<FinancialProduct, Long> {
}
