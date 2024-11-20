package com.adepuu.fezz4ubackend.infrastructure.auth.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LogoutRequestDTO {
    @NotNull
    private String refreshToken;
    @NotNull
    private String accessToken;
}