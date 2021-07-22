package com.plugchecker.backend.domain.etc;

import com.plugchecker.backend.IntegrationTest;
import com.plugchecker.backend.domain.auth.dto.request.EmailRequest;
import com.plugchecker.backend.domain.auth.dto.request.RefreshTokenRequest;
import com.plugchecker.backend.domain.auth.dto.request.SignInRequest;
import com.plugchecker.backend.domain.auth.dto.request.SignUpRequest;
import com.plugchecker.backend.domain.auth.dto.response.TokenResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class AuthDtoClassTest extends IntegrationTest {

    @Test
    @DisplayName("auth request dto 테스트")
    void authRequestDto_Test() {
        // EmailRequest
        EmailRequest emailRequest1 = new EmailRequest();
        Assertions.assertNotNull(emailRequest1);
        EmailRequest emailRequest2 = new EmailRequest("xxxxxxx@gmail.com", "1111");
        Assertions.assertEquals("xxxxxxx@gmail.com", emailRequest2.getEmail());
        Assertions.assertEquals("1111", emailRequest2.getNumber());

        // RefreshTokenRequest
        RefreshTokenRequest refreshTokenRequest1 = new RefreshTokenRequest();
        Assertions.assertNotNull(refreshTokenRequest1);
        RefreshTokenRequest refreshTokenRequest2 = new RefreshTokenRequest("refreshToken");
        Assertions.assertEquals("refreshToken", refreshTokenRequest2.getRefreshToken());

        // SignInRequest
        SignInRequest signInRequest1 = new SignInRequest();
        Assertions.assertNotNull(signInRequest1);
        SignInRequest signInRequest2 = new SignInRequest("testUserId", "testUserPassword");
        Assertions.assertEquals("testUserId", signInRequest2.getId());
        Assertions.assertEquals("testUserPassword", signInRequest2.getPassword());

        // SignUpRequest
        SignUpRequest signUpRequest1 = new SignUpRequest();
        Assertions.assertNotNull(signUpRequest1);
        SignUpRequest signUpRequest2 = new SignUpRequest("testUserId", "testUserPassword", "xxxxxx@gmail.com");
        Assertions.assertEquals("testUserId", signInRequest2.getId());
        Assertions.assertEquals("testUserPassword", signInRequest2.getPassword());
        Assertions.assertEquals("xxxxxx@gmail.com", signUpRequest2.getEmail());
    }

    @Test
    @DisplayName("auth response dto 테스트")
    void authResponseDto_Test() {
        // TokenResponse
        TokenResponse tokenResponse = new TokenResponse("accessToken", "refreshToken");
        Assertions.assertEquals("accessToken", tokenResponse.getAccessToken());
        Assertions.assertEquals("refreshToken", tokenResponse.getRefreshToken());
    }
}
