package com.plugchecker.backend.domain.security;

import com.plugchecker.backend.ApiTest;
import com.plugchecker.backend.domain.auth.domain.User;
import com.plugchecker.backend.domain.auth.domain.UserRepository;
import com.plugchecker.backend.global.security.JwtTokenProvider;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class JwtTokenFilterTest extends ApiTest {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @Autowired
    private UserRepository userRepository;

    @Test
    void existsToken_Test() throws Exception {
        // given
        String id = "hello";

        User user = User.builder()
                .id(id)
                .password("testUserPassword")
                .email("xxxxxx@gmail.com")
                .build();
        userRepository.save(user);

        String token = jwtTokenProvider.generateAccessToken(id);

        // when
        ResultActions resultActions = requestMvc(get("/testFilter").header("AUTHORIZATION", "Bearer " + token));

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("token").value(true))
                .andDo(print())
                .andReturn();
    }

    @Test
    void nonExistsToken_Test() throws Exception {
        // when
        ResultActions resultActions = requestMvc(get("/testFilter"));

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("token").value(false))
                .andDo(print())
                .andReturn();
    }

    @Test
    void notAccessToken_Test() throws Exception {
        // given
        String id = "hello";

        String token = jwtTokenProvider.generateRefreshToken(id);

        // when
        ResultActions resultActions = requestMvc(get("/testFilter").header("AUTHORIZATION", "Bearer " + token));

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("token").value(false))
                .andDo(print())
                .andReturn();
    }
}
