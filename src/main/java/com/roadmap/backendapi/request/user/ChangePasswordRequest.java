package com.roadmap.backendapi.request.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChangePasswordRequest {
    @NotBlank
    private String password;

    @NotBlank
    @Size(min = 8, max = 20)
    private String newPassword;

    @NotBlank
    @Size(min = 8, max = 20)
    private String confirmPassword;
}
