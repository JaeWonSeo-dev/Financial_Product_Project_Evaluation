package com.portfolio.finance.controller;

import com.portfolio.finance.domain.ProjectStatus;
import com.portfolio.finance.dto.CashFlowInput;
import com.portfolio.finance.dto.ProjectRequest;
import com.portfolio.finance.service.ProjectEvaluationService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/projects")
public class ProjectController {
    private final ProjectEvaluationService service;
    public ProjectController(ProjectEvaluationService service) { this.service = service; }
    @GetMapping public String list(Model model) { model.addAttribute("projects", service.findAll()); return "projects/list"; }
    @GetMapping("/new") public String createForm(Model model) { fill(model, baseRequest(), false, null); return "projects/form"; }
    @PostMapping public String create(@Valid @ModelAttribute("project") ProjectRequest request, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) { if (bindingResult.hasErrors()) { fill(model, ensureFlows(request), false, null); return "projects/form"; } var saved = service.create(request); redirectAttributes.addFlashAttribute("message", "프로젝트가 등록되었습니다."); return "redirect:/projects/" + saved.getId(); }
    @GetMapping("/{id}") public String detail(@PathVariable Long id, Model model) { model.addAttribute("project", service.findById(id)); model.addAttribute("evaluation", service.evaluate(id)); return "projects/detail"; }
    @GetMapping("/{id}/edit") public String editForm(@PathVariable Long id, Model model) { var p = service.findById(id); ProjectRequest req = new ProjectRequest(); req.setName(p.getName()); req.setCategory(p.getCategory()); req.setInitialInvestment(p.getInitialInvestment()); req.setDiscountRate(p.getDiscountRate()); req.setStatus(p.getStatus()); req.setOwner(p.getOwner()); req.setStartDate(p.getStartDate()); req.setEndDate(p.getEndDate()); req.setDescription(p.getDescription()); List<CashFlowInput> flows = new ArrayList<>(); p.getCashFlows().forEach(cf -> { CashFlowInput input = new CashFlowInput(); input.setPeriodYear(cf.getPeriodYear()); input.setInflow(cf.getInflow()); input.setOutflow(cf.getOutflow()); flows.add(input); }); req.setCashFlows(flows); fill(model, ensureFlows(req), true, id); return "projects/form"; }
    @PutMapping("/{id}") public String update(@PathVariable Long id, @Valid @ModelAttribute("project") ProjectRequest request, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) { if (bindingResult.hasErrors()) { fill(model, ensureFlows(request), true, id); return "projects/form"; } service.update(id, request); redirectAttributes.addFlashAttribute("message", "프로젝트가 수정되었습니다."); return "redirect:/projects/" + id; }
    @DeleteMapping("/{id}") public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) { service.delete(id); redirectAttributes.addFlashAttribute("message", "프로젝트가 삭제되었습니다."); return "redirect:/projects"; }
    private ProjectRequest baseRequest() { ProjectRequest request = new ProjectRequest(); request.setCashFlows(List.of(new CashFlowInput(), new CashFlowInput(), new CashFlowInput(), new CashFlowInput())); return request; }
    private ProjectRequest ensureFlows(ProjectRequest request) { if (request.getCashFlows() == null || request.getCashFlows().isEmpty()) request.setCashFlows(baseRequest().getCashFlows()); return request; }
    private void fill(Model model, ProjectRequest request, boolean edit, Long id) { model.addAttribute("project", request); model.addAttribute("projectStatuses", ProjectStatus.values()); model.addAttribute("editMode", edit); model.addAttribute("projectId", id); }
}
