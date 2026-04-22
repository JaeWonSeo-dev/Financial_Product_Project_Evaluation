package com.portfolio.finance.controller.api;

import com.portfolio.finance.domain.EvaluationProject;
import com.portfolio.finance.domain.ProjectEvaluationSnapshot;
import com.portfolio.finance.dto.ProjectEvaluationResponse;
import com.portfolio.finance.dto.ProjectRequest;
import com.portfolio.finance.service.ProjectEvaluationService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
public class ProjectApiController {
    private final ProjectEvaluationService service;
    public ProjectApiController(ProjectEvaluationService service) { this.service = service; }
    @GetMapping public List<EvaluationProject> findAll() { return service.findAll(); }
    @GetMapping("/{id}") public EvaluationProject findOne(@PathVariable Long id) { return service.findById(id); }
    @PostMapping public EvaluationProject create(@Valid @RequestBody ProjectRequest request) { return service.create(request); }
    @PutMapping("/{id}") public EvaluationProject update(@PathVariable Long id, @Valid @RequestBody ProjectRequest request) { return service.update(id, request); }
    @DeleteMapping("/{id}") public void delete(@PathVariable Long id) { service.delete(id); }
    @GetMapping("/{id}/evaluation") public ProjectEvaluationResponse evaluate(@PathVariable Long id) { return service.evaluate(id); }
    @GetMapping("/{id}/history") public List<ProjectEvaluationSnapshot> history(@PathVariable Long id) { return service.findRecentSnapshots(id); }
}
