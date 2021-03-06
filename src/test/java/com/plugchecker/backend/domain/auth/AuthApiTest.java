package com.plugchecker.backend.domain.auth;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.plugchecker.backend.domain.auth.domain.RefreshTokenRepository;
import com.plugchecker.backend.domain.auth.domain.User;
import com.plugchecker.backend.domain.auth.domain.UserRepository;
import com.plugchecker.backend.domain.auth.dto.request.EmailRequest;
import com.plugchecker.backend.domain.auth.dto.request.RefreshTokenRequest;
import com.plugchecker.backend.domain.auth.dto.request.SignInRequest;
import com.plugchecker.backend.domain.auth.dto.request.SignUpRequest;
import com.plugchecker.backend.domain.auth.dto.response.TokenResponse;
import com.plugchecker.backend.domain.auth.service.SignUpService;
import com.plugchecker.backend.global.error.exception.NotFoundException;
import com.plugchecker.backend.global.security.JwtTokenProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
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

    @MockBean
    private SignUpService signUpService;

    @Test
    @DisplayName("????????? ???????????? ??????")
    void checkNumber() throws Exception {
        // given
        String email = "xxxxxxx@gmail.com";
        String number = "9999";
        String accessToken = "accessToken";
        String refreshToken = "refreshToken";

        EmailRequest request = new EmailRequest(email, number);
        TokenResponse response = new TokenResponse(accessToken, refreshToken);

        given(signUpService.checkNumber(any())).willReturn(response);

        // when
        ResultActions resultActions = requestCheckNumber(request);

        // then
        resultActions.andExpect(status().isCreated())
                .andExpect(jsonPath("accessToken").value(accessToken))
                .andExpect(jsonPath("refreshToken").value(refreshToken))
                .andDo(print());
    }

    @Test
    @DisplayName("???????????? ????????? ???????????? ??????")
    void signUp() throws Exception {
        // given
        String id = "testUserId";
        String password = "testUserPassword";
        String email = "xxxxxxx@gmail.com";

        SignUpRequest request = new SignUpRequest(id, password, email);

        // when
        ResultActions resultActions = requestSignUp(request);

        // then
        resultActions.andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("?????????")
    void login() throws Exception {
        // given
        String id = "testUser";
        String password = "password123123#";
        String email = "xxxxxxxx@gmail.com";
        User user = User.builder()
                .id(id)
                .password(passwordEncoder.encode(password))
                .email(email)
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

        String findId = jwtTokenProvider.getId(response.getRefreshToken());
        User findUser = userRepository.findById(findId)
                .orElseThrow(()-> new NotFoundException(findId));

        Assertions.assertEquals(id, findId);
        Assertions.assertEquals(user.getPassword(), findUser.getPassword());
        Assertions.assertEquals(email, findUser.getEmail());
    }

    @Test
    @DisplayName("??????????????????")
    void tokenRefresh() throws Exception {
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
