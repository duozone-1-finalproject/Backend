package com.example.finalproject.ai_backend.dto.validation;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
public class ValidationDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private String guide;
    private Quality quality;
    private String decision;
    private List<Issue> issues;
    private String notes;

    @Data
    public static class Quality implements Serializable {
        @Serial
        private static final long serialVersionUID = 1L;

        private Integer guideline_adherence;
        private Integer factuality;
        private Integer clarity;
    }

    @Data
    public static class Issue implements Serializable {
        @Serial
        private static final long serialVersionUID = 1L;

        private String span;
        private String reason;
        private String ruleId;
        private String evidence;
        private String suggestion;
        private String severity;
    }
}
