package com.plugchecker.backend.domain.auth;

import com.plugchecker.backend.ServiceTest;
import com.plugchecker.backend.domain.auth.domain.CertificationNumber;
import com.plugchecker.backend.domain.auth.domain.CertificationNumberRepository;
import com.plugchecker.backend.domain.auth.domain.RefreshTokenRepository;
import com.plugchecker.backend.domain.auth.domain.UserRepository;
import com.plugchecker.backend.domain.auth.dto.request.EmailRequest;
import com.plugchecker.backend.domain.auth.dto.request.SignUpRequest;
import com.plugchecker.backend.domain.auth.dto.response.TokenResponse;
import com.plugchecker.backend.domain.auth.service.EmailSendService;
import com.plugchecker.backend.domain.auth.service.SignUpService;
import com.plugchecker.backend.global.security.JwtTokenProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.mockito.BDDMockito.given;

public class SignUpServiceTest extends ServiceTest {
    @Mock
    private CertificationNumberRepository certificationNumberRepository;
    @Mock
    private RefreshTokenRepository refreshTokenRepository;
    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private EmailSendService emailSendService;
    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @InjectMocks
    private SignUpService signUpService;

    @Test
    void checkNumber_테스트() {
        // given
        String email = "xxxxxxx@gmail.com";
        String number = "9999";
        String id = "testUserId";
        String password = "testUserPassword";
        String accessToken = "accessToken";
        String refreshToken = "refreshToken";

        EmailRequest request = new EmailRequest(email, number);

        CertificationNumber certificationNumber = CertificationNumber.builder()
                .id(1L)
                .email(email)
                .number(number)
                .user_id(id)
                .user_password(password)
                .exp(1000L)
                .build();
        given(certificationNumberRepository.findByEmail(email))
                .willReturn(Optional.of(certificationNumber));
        given(jwtTokenProvider.generateAccessToken(id)).willReturn(accessToken);
        given(jwtTokenProvider.generateRefreshToken(id)).willReturn(refreshToken);

        // when
        TokenResponse response = signUpService.checkNumber(request);

        // then
        Assertions.assertEquals(accessToken, response.getAccessToken());
        Assertions.assertEquals(refreshToken, response.getRefreshToken());
    }

    @Test
    void signUp_테스트() {
        // given
        String id = "testUserId";
        String password = "testUserPassword";
        String email = "xxxxxxx@gmail.com";

        SignUpRequest request = new SignUpRequest(id, password, email);

        given(userRepository.existsById(id)).willReturn(false);
        given(userRepository.existsByEmail(email)).willReturn(false);

        // when then
        signUpService.signUp(request);
    }
}
