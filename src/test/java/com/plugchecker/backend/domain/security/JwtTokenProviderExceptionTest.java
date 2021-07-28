package com.plugchecker.backend.domain.security;

import com.plugchecker.backend.IntegrationTest;
import com.plugchecker.backend.global.error.exception.InvalidTokenException;
import com.plugchecker.backend.global.security.JwtTokenProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class JwtTokenProviderExceptionTest extends IntegrationTest {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Test
    void getId_InvalidTokenException_Test() {
        // given
        String token = "strangeToken";

        // when, then
        Assertions.assertThrows(InvalidTokenException.class, ()-> jwtTokenProvider.getId(token));
    }

    @Test
    void checkToken_InvalidTokenException_Test() {
        // given
        String token = "strangeToken";

        // when, then
        Assertions.assertThrows(InvalidTokenException.class, ()-> jwtTokenProvider.isAccessToken(token));
        Assertions.assertThrows(InvalidTokenException.class, ()-> jwtTokenProvider.isRefreshToken(token));
    }
}
