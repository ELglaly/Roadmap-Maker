package com.roadmap.backendapi.dto;

import com.roadmap.backendapi.entity.enums.ResourceType;
import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.URL;

import java.io.Serializable;

/**
 * DTO for {@link com.roadmap.backendapi.entity.Resource}
 */
public record ResourceDTO(@Size(message = "Title cannot exceed 255 characters", max = 255)
                          @NotBlank(message = "Title is required")
                          String title,
                          @NotNull(message = "Resource type is required")
                          ResourceType type,
                          @NotNull @Size(message = "URL too long", max = 2048)
                          @Pattern(message = "Only HTTP/HTTPS URLs are allowed", regexp = "^https?://.*")
                          @NotEmpty
                          @NotBlank(message = "URL is required")
                          @URL(message = "Invalid URL format") String url)
        implements Serializable {

  }