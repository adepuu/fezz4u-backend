package com.adepuu.fezz4ubackend.infrastructure.auth.controller;

import com.adepuu.fezz4ubackend.infrastructure.auth.dto.LogoutRequestDTO;
import com.adepuu.fezz4ubackend.usecase.auth.LogoutUsecase;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.adepuu.fezz4ubackend.common.response.Response;
import com.adepuu.fezz4ubackend.infrastructure.auth.Claims;
import com.adepuu.fezz4ubackend.infrastructure.auth.dto.LoginRequestDTO;
import com.adepuu.fezz4ubackend.usecase.auth.LoginUsecase;
import com.adepuu.fezz4ubackend.usecase.auth.TokenRefreshUsecase;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final LoginUsecase loginUsecase;
    private final TokenRefreshUsecase tokenRefreshUsecase;
private final LogoutUsecase logoutUsecase;

    public AuthController(LoginUsecase loginUsecase, TokenRefreshUsecase tokenRefreshUsecase,
                          com.adepuu.fezz4ubackend.usecase.auth.TokenBlacklistUsecase tokenBlacklistUsecase, LogoutUsecase logoutUsecase) {
        this.loginUsecase = loginUsecase;
        this.tokenRefreshUsecase = tokenRefreshUsecase;
        this.logoutUsecase = logoutUsecase;

    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Validated @RequestBody LoginRequestDTO req) {
        return Response.successfulResponse("Login successful", loginUsecase.authenticateUser(req));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@Validated @RequestBody LogoutRequestDTO req) {
      var accessToken = Claims.getJwtTokenString();
      req.setAccessToken(accessToken);
      return Response.successfulResponse("Logout successful", logoutUsecase.logoutUser(req));
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh() {
        String tokenType = Claims.getTokenTypeFromJwt();
        if (!"REFRESH".equals(tokenType)) {
            return Response.failedResponse(HttpStatus.UNAUTHORIZED.value(), "Invalid token type for refresh");
        }
        String token = Claims.getJwtTokenString();
        return Response.successfulResponse("Refresh successful", tokenRefreshUsecase.refreshAccessToken(token));
    }
}
