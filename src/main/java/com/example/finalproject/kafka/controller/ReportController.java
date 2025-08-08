// 파일 경로: .../com/example/finalproject/kafka/controller/ReportController.java
package com.example.finalproject.kafka.controller;

import com.example.finalproject.kafka.dto.ReportCreationRequest;
import com.example.finalproject.kafka.dto.ReportResult;
import com.example.finalproject.kafka.service.ReportRequestManager;
import com.example.finalproject.kafka.service.ReportService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/reports")
public class ReportController {
    private final ReportService reportService;
    private final ReportRequestManager requestManager;

    public ReportController(ReportService reportService, ReportRequestManager requestManager) {
        this.reportService = reportService;
        this.requestManager = requestManager;
    }

    @PostMapping
    public ResponseEntity<Map<String, String>> createReport(@RequestBody ReportCreationRequest request) {
        String requestId = reportService.submitReportRequest(request);
        String statusUrl = "/api/reports/" + requestId + "/status";
        Map<String, String> responseBody = Map.of("requestId", requestId, "statusUrl", statusUrl);
        return ResponseEntity.accepted().location(URI.create(statusUrl)).body(responseBody);
    }

    @GetMapping("/{requestId}/status")
    public ResponseEntity<?> getReportStatus(@PathVariable String requestId) {
        ReportRequestManager.RequestStatus status = requestManager.getStatus(requestId);
        if (status == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Request not found with ID: " + requestId));
        }
        Map<String, Object> response = new HashMap<>();
        response.put("requestId", requestId);
        response.put("status", status);
        if (status == ReportRequestManager.RequestStatus.COMPLETED) {
            Optional<ReportResult> result = requestManager.getResult(requestId);
            result.ifPresent(res -> response.put("result", res));
        }
        return ResponseEntity.ok(response);
    }
}
