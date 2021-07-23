package com.plugchecker.backend.domain.auth;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.plugchecker.backend.domain.auth.domain.RefreshTokenRepository;
import com.plugchecker.backend.domain.auth.domain.User;
import com.plugchecker.backend.domain.auth.domain.UserRepository;
import com.plugchecker.backend.domain.auth.dto.request.RefreshTokenRequest;
import com.plugchecker.backend.domain.auth.dto.request.SignInRequest;
import com.plugchecker.backend.domain.auth.dto.response.TokenResponse;
import com.plugchecker.backend.global.security.JwtTokenProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AuthApiTest extends AuthApiRequest{

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @Autowired
    private RefreshTokenRepository refreshTokenRepository;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("로그인")
    void login_Test() throws Exception {
        // given
        String id = "testUser";
        String password = "password123123#";
        User user = User.builder()
                .id(id)
                .password(passwordEncoder.encode(password))
                .email("xxxxxxxx@gmail.com")
                .build();
        userRepository.save(user);
        SignInRequest request = new SignInRequest(id, password);

        // when
        ResultActions resultActions = requestLogin(request);

        // then
        MvcResult result = resultActions.andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        TokenResponse response = objectMapper.readValue(
                result.getResponse().getContentAsString(), new TypeReference<TokenResponse>() {});

        Assertions.assertEquals(jwtTokenProvider.getId(response.getRefreshToken()), id);
    }

    @Test
    @DisplayName("토큰리프레쉬")
    void tokenRefresh_Test() throws Exception {
        // given
        String id = "testUser";
        String refresh = jwtTokenProvider.generateRefreshToken(id);
        refreshTokenRepository.saveRefreshToken(refresh, 1000L);
        RefreshTokenRequest request = new RefreshTokenRequest(refresh);

        // when
        ResultActions resultActions = requestTokenRefresh(request);

        // then
        MvcResult result = resultActions.andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        TokenResponse response = objectMapper.readValue(
                result.getResponse().getContentAsString(), new TypeReference<TokenResponse>() {});

        Assertions.assertEquals(jwtTokenProvider.getId(response.getRefreshToken()), id);

    }
}
