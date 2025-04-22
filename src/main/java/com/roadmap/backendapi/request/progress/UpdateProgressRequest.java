package com.roadmap.backendapi.request.progress;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Setter
@Getter
public class UpdateProgressRequest {
    private Timestamp completed_at;
    private Long userId;
    private Long milestoneId;
    private Long progressId;
}
