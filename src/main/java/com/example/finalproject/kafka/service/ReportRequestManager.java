// 파일 경로: .../com/example/finalproject/kafka/service/ReportRequestManager.java
package com.example.finalproject.kafka.service;

import com.example.finalproject.kafka.dto.ReportResult;
import org.springframework.stereotype.Component;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ReportRequestManager {
    public enum RequestStatus { PENDING, COMPLETED, FAILED }
    private final Map<String, RequestStatus> requestStatuses = new ConcurrentHashMap<>();
    private final Map<String, ReportResult> requestResults = new ConcurrentHashMap<>();

    public void createRequest(String requestId) { requestStatuses.put(requestId, RequestStatus.PENDING); }
    public void completeRequest(String requestId, ReportResult result) {
        requestStatuses.put(requestId, RequestStatus.COMPLETED);
        requestResults.put(requestId, result);
    }
    public RequestStatus getStatus(String requestId) { return requestStatuses.get(requestId); }
    public Optional<ReportResult> getResult(String requestId) { return Optional.ofNullable(requestResults.get(requestId)); }
}
