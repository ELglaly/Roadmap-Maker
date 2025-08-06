package com.roadmap.backendapi.request.progress;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Setter
@Getter
public class UpdateProgressRequest {
    BigDecimal percentage;
    private Long progressId;
}
