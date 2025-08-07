package com.roadmap.backendapi.entity.enums;

public enum ValidationPatterns {
    STRING_PATTERN("^[A-Za-z\\s'-]{2,50}$", "must be 2-50 characters, letters, spaces, hyphens and apostrophes only"),
    ZIP_PATTERN("^[A-Z0-9\\s-]{3,15}$", "ZIP code must be 3-15 characters, alphanumeric with spaces and hyphens");

}
