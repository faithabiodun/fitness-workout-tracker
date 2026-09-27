package com.example.fitnessworkouttracker.report;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/progress")
    public ProgressReportResponse progress(Authentication auth) {
        return reportService.getProgressReport(Long.parseLong(auth.getName()));
    }
}
