package com.plugchecker.backend.domain.auth.controller;

import com.plugchecker.backend.domain.auth.dto.request.EmailRequest;
import com.plugchecker.backend.domain.auth.dto.request.RefreshTokenRequest;
import com.plugchecker.backend.domain.auth.dto.request.SignInRequest;
import com.plugchecker.backend.domain.auth.dto.request.SignUpRequest;
import com.plugchecker.backend.domain.auth.dto.response.TokenResponse;
import com.plugchecker.backend.domain.auth.service.SignInService;
import com.plugchecker.backend.domain.auth.service.SignUpService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final SignUpService signUpService;
    private final SignInService signInService;

    @PostMapping("/email")
    @ResponseStatus(HttpStatus.CREATED)
    public TokenResponse checkNumber(@Valid @RequestBody EmailRequest request) {
        return signUpService.checkNumber(request);
    }

    @PostMapping("/sign-up")
    public void signUp(@Valid @RequestBody SignUpRequest request) {
        signUpService.signUp(request);
    }

    @PostMapping("/login")
    public TokenResponse signIn(@Valid @RequestBody SignInRequest request) {
        return signInService.signIn(request);
    }

    @PostMapping("/token-refresh")
    public TokenResponse tokenRefresh(@Valid @RequestBody RefreshTokenRequest request) {
        return signInService.tokenRefresh(request.getRefreshToken());
    }
}
