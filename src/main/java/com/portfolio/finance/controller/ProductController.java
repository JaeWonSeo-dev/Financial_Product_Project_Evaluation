package com.portfolio.finance.controller;

import com.portfolio.finance.domain.ProductType;
import com.portfolio.finance.domain.RiskGrade;
import com.portfolio.finance.dto.ProductRequest;
import com.portfolio.finance.service.FinancialProductService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/products")
public class ProductController {
    private final FinancialProductService service;
    public ProductController(FinancialProductService service) { this.service = service; }
    @GetMapping public String list(Model model) { model.addAttribute("products", service.findAll()); return "products/list"; }
    @GetMapping("/new") public String createForm(Model model) { fill(model, new ProductRequest(), false); return "products/form"; }
    @PostMapping public String create(@Valid @ModelAttribute("product") ProductRequest request, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) { if (bindingResult.hasErrors()) { fill(model, request, false); return "products/form"; } service.create(request); redirectAttributes.addFlashAttribute("message", "금융상품이 등록되었습니다."); return "redirect:/products"; }
    @GetMapping("/{id}") public String detail(@PathVariable Long id, Model model) { model.addAttribute("product", service.findById(id)); return "products/detail"; }
    @GetMapping("/{id}/edit") public String editForm(@PathVariable Long id, Model model) { var p=service.findById(id); ProductRequest req=new ProductRequest(); req.setName(p.getName()); req.setProductType(p.getProductType()); req.setInvestmentAmount(p.getInvestmentAmount()); req.setExpectedReturnRate(p.getExpectedReturnRate()); req.setDiscountRate(p.getDiscountRate()); req.setMaturityMonths(p.getMaturityMonths()); req.setRiskGrade(p.getRiskGrade()); req.setAnnualVolatility(p.getAnnualVolatility()); req.setIssuer(p.getIssuer()); req.setDescription(p.getDescription()); fill(model, req, true); model.addAttribute("productId", id); return "products/form"; }
    @PutMapping("/{id}") public String update(@PathVariable Long id, @Valid @ModelAttribute("product") ProductRequest request, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) { if (bindingResult.hasErrors()) { fill(model, request, true); model.addAttribute("productId", id); return "products/form"; } service.update(id, request); redirectAttributes.addFlashAttribute("message", "금융상품이 수정되었습니다."); return "redirect:/products/" + id; }
    @DeleteMapping("/{id}") public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) { service.delete(id); redirectAttributes.addFlashAttribute("message", "금융상품이 삭제되었습니다."); return "redirect:/products"; }
    private void fill(Model model, ProductRequest request, boolean edit) { model.addAttribute("product", request); model.addAttribute("productTypes", ProductType.values()); model.addAttribute("riskGrades", RiskGrade.values()); model.addAttribute("editMode", edit); }
}
