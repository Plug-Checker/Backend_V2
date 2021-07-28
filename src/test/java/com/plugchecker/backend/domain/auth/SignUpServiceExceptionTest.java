package com.plugchecker.backend.domain.auth;

import com.plugchecker.backend.ServiceTest;
import com.plugchecker.backend.domain.auth.domain.CertificationNumber;
import com.plugchecker.backend.domain.auth.domain.CertificationNumberRepository;
import com.plugchecker.backend.domain.auth.domain.RefreshTokenRepository;
import com.plugchecker.backend.domain.auth.domain.UserRepository;
import com.plugchecker.backend.domain.auth.dto.request.EmailRequest;
import com.plugchecker.backend.domain.auth.dto.request.SignUpRequest;
import com.plugchecker.backend.domain.auth.service.EmailSendService;
import com.plugchecker.backend.domain.auth.service.SignUpService;
import com.plugchecker.backend.global.error.exception.AlreadyExistException;
import com.plugchecker.backend.global.error.exception.InvalidCertificationNumberException;
import com.plugchecker.backend.global.error.exception.NotFoundException;
import com.plugchecker.backend.global.security.JwtTokenProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.mockito.BDDMockito.given;

public class SignUpServiceExceptionTest extends ServiceTest {
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
    void checkNumber_NotFoundException_테스트() {
        // given
        String email = "xxxxxxx@gmail.com";
        String number = "9999";

        EmailRequest request = new EmailRequest(email, number);

        given(certificationNumberRepository.findByEmail(email))
                .willReturn(Optional.empty());


        // when, then
        Assertions.assertThrows(NotFoundException.class, ()-> signUpService.checkNumber(request));
    }

    @Test
    void checkNumber_InvalidCertificationNumberException_테스트() {
        // given
        String email = "xxxxxxx@gmail.com";
        String number = "9999";
        String wrongNumber = "8888";
        String id = "testUserId";
        String password = "testUserPassword";

        EmailRequest request = new EmailRequest(email, wrongNumber);

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

        // when, then
        Assertions.assertThrows(InvalidCertificationNumberException.class, ()-> signUpService.checkNumber(request));
    }

    @Test
    void signUp_Id_AlreadyExistException테스트() {
        // given
        String id = "testUserId";
        String password = "testUserPassword";
        String email = "xxxxxxx@gmail.com";

        SignUpRequest request = new SignUpRequest(id, password, email);

        given(userRepository.existsById(id)).willReturn(true);

        // when then
        Assertions.assertThrows(AlreadyExistException.class, ()-> signUpService.signUp(request));
    }

    @Test
    void signUp_Email_AlreadyExistException테스트() {
        // given
        String id = "testUserId";
        String password = "testUserPassword";
        String email = "xxxxxxx@gmail.com";

        SignUpRequest request = new SignUpRequest(id, password, email);

        given(userRepository.existsById(id)).willReturn(false);
        given(userRepository.existsByEmail(email)).willReturn(true);

        // when then
        Assertions.assertThrows(AlreadyExistException.class, ()-> signUpService.signUp(request));
    }
}
