package com.plugchecker.backend.domain.auth;

import com.plugchecker.backend.ServiceTest;
import com.plugchecker.backend.domain.auth.controller.AuthController;
import com.plugchecker.backend.domain.auth.dto.request.EmailRequest;
import com.plugchecker.backend.domain.auth.dto.request.SignUpRequest;
import com.plugchecker.backend.domain.auth.dto.response.TokenResponse;
import com.plugchecker.backend.domain.auth.service.EmailSendService;
import com.plugchecker.backend.domain.auth.service.SignInService;
import com.plugchecker.backend.domain.auth.service.SignUpService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.BDDMockito.given;

public class AuthControllerSignUpTest extends ServiceTest {

    @Mock
    private SignUpService signUpService;
    @InjectMocks
    private AuthController authController;

    @Test
    void checkNumberTest() {
        // given
        EmailRequest request = new EmailRequest("xxxxxxx@gmail.com", "number");
        TokenResponse expectResponse = new TokenResponse("accessToken", "refreshToken");
        given(signUpService.checkNumber(request)).willReturn(expectResponse);

        // when
        TokenResponse response = authController.checkNumber(request);

        // then
        Assertions.assertEquals(response.getAccessToken(), expectResponse.getAccessToken());
        Assertions.assertEquals(response.getRefreshToken(), expectResponse.getRefreshToken());
    }

    @Test
    void signUpTest() {
        // given
        SignUpRequest request = new SignUpRequest("idid123123", "password123#", "xxxxxxxx@gmail.com");
        // when, then
        authController.signUp(request);
    }
}
