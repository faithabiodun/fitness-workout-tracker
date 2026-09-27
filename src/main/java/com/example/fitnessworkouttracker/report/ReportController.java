package com.example.fitnessworkouttracker.report;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reports")
public class ReportController {
    // I'm serving my private progress reports under /api/reports
    private final ReportService reportService;

    public ReportController(ReportService reportService) { // I'm delegating my math to ReportService
        this.reportService = reportService;
    }

    @GetMapping("/progress") // I'm keeping this authenticated so I read the user from the JWT
    public ProgressReportResponse progress(Authentication auth) {
        return reportService.getProgressReport(Long.parseLong(auth.getName())); // I learned auth.getName() holds my user id from the token
    }
}
