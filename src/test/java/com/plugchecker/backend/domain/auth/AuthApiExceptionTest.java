package com.plugchecker.backend.domain.auth;

import com.plugchecker.backend.domain.auth.domain.RefreshTokenRepository;
import com.plugchecker.backend.domain.auth.domain.User;
import com.plugchecker.backend.domain.auth.domain.UserRepository;
import com.plugchecker.backend.domain.auth.dto.request.RefreshTokenRequest;
import com.plugchecker.backend.domain.auth.dto.request.SignInRequest;
import com.plugchecker.backend.global.security.JwtTokenProvider;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AuthApiExceptionTest extends AuthApiRequest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Test
    void signIn_NotFoundException_Test() throws Exception {
        // given
        String fakeId = "testUserIdFake";
        String fakePassword = "testUserPasswordFake";
        String id = "testUserId";
        String password = "testUserPassword";

        User user = User.builder()
                .id(id)
                .password(password)
                .email("xxxxxxxx@gmail.com")
                .build();
        userRepository.save(user);

        SignInRequest request1 = new SignInRequest(fakeId, fakePassword);
        SignInRequest request2 = new SignInRequest(id, password);

        // when
        ResultActions resultActions1 = requestLogin(request1);
        ResultActions resultActions2 = requestLogin(request2);

        // then
        resultActions1.andExpect(status().is4xxClientError())
                .andDo(print());
        resultActions2.andExpect(status().is4xxClientError())
                .andDo(print());
    }

    @Test
    void tokenRefresh_InvalidTokenException_Test() throws Exception {
        // given
        String id = "testTEST";
        String refreshToken1 = "refreshTokenToken123123";
        String refreshToken2 = jwtTokenProvider.generateRefreshToken(id);
        refreshTokenRepository.saveRefreshToken(refreshToken1, 1000L);

        RefreshTokenRequest request1 = new RefreshTokenRequest(refreshToken1);
        RefreshTokenRequest request2 = new RefreshTokenRequest(refreshToken2);

        // when
        ResultActions resultActions1 = requestTokenRefresh(request1);
        ResultActions resultActions2 = requestTokenRefresh(request2);

        // then
        resultActions1.andExpect(status().is4xxClientError())
                .andDo(print());
        resultActions2.andExpect(status().is4xxClientError())
                .andDo(print());
    }
}
