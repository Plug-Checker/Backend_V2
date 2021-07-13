package com.plugchecker.backend.domain.auth.service;

import com.plugchecker.backend.domain.auth.dto.request.SignInRequest;
import com.plugchecker.backend.domain.auth.dto.response.TokenResponse;
import org.springframework.stereotype.Service;

@Service
public class SignInService {

    public TokenResponse signIn(SignInRequest request) {
        return null;
    }

    public TokenResponse tokenRefresh(String refreshToken) {
        return null;
    }
}
