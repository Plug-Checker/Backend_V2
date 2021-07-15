package com.plugchecker.backend.domain.security;

import com.plugchecker.backend.IntegrationTest;
import com.plugchecker.backend.global.security.JwtTokenProvider;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.Base64;
import java.util.Date;

public class JwtTokenProviderTest extends IntegrationTest {

    @Value("${auth.jwt.secret}")
    private String secretKey;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Test
    void generateAccessToken_Test() {
        // given
        String expectedId = "userId";

        // when
        String accessToken = jwtTokenProvider.generateAccessToken(expectedId);

        // then
        String type = Jwts.parser().setSigningKey(encodingSecretKey())
                .parseClaimsJws(accessToken).getBody().get("type", String.class);
        String actualId = Jwts.parser().setSigningKey(encodingSecretKey())
                .parseClaimsJws(accessToken).getBody().getSubject();
        Assertions.assertEquals("access", type);
        Assertions.assertEquals(expectedId, actualId);
    }

    @Test
    void generateRefreshToken_Test() {
        // given
        String expectedId = "userId";

        // when
        String accessToken = jwtTokenProvider.generateRefreshToken(expectedId);

        // then
        String type = Jwts.parser().setSigningKey(encodingSecretKey())
                .parseClaimsJws(accessToken).getBody().get("type", String.class);
        String actualId = Jwts.parser().setSigningKey(encodingSecretKey())
                .parseClaimsJws(accessToken).getBody().getSubject();
        Assertions.assertEquals("refresh", type);
        Assertions.assertEquals(expectedId, actualId);
    }

    @Test
    void isAccessToken_Test() {
        // given
        String id = "testId";
        String accessToken = makeToken(id, "access", 1000L);
        String refreshToken = makeToken(id, "refresh", 1000L);

        // when
        boolean accessTokenResponse = jwtTokenProvider.isAccessToken(accessToken);
        boolean refreshTokenResponse = jwtTokenProvider.isAccessToken(refreshToken);

        // then
        Assertions.assertTrue(accessTokenResponse);
        Assertions.assertFalse(refreshTokenResponse);
    }

    @Test
    void isRefreshToken_Test() {
        // given
        String id = "testId";
        String accessToken = makeToken(id, "access", 1000L);
        String refreshToken = makeToken(id, "refresh", 1000L);

        // when
        boolean accessTokenResponse = jwtTokenProvider.isRefreshToken(accessToken);
        boolean refreshTokenResponse = jwtTokenProvider.isRefreshToken(refreshToken);

        // then
        Assertions.assertFalse(accessTokenResponse);
        Assertions.assertTrue(refreshTokenResponse);
    }

    @Test
    void getId_Test() {
        // given
        String expectedId = "testId";
        String token = makeToken(expectedId, "access", 10000L);

        // when
        String actualId = jwtTokenProvider.getId(token);

        // then
        Assertions.assertEquals(expectedId, actualId);
    }

    @Test
    void checkToken() {
        // given
        String token = "Bearer tokentokentoken";

        // when
        Boolean response = jwtTokenProvider.checkToken(token);

        // then
        Assertions.assertTrue(response);
    }

    private String makeToken(String id, String type, Long time) {
        return Jwts.builder()
                .setExpiration(new Date(System.currentTimeMillis() + (time)))
                .signWith(SignatureAlgorithm.HS512, encodingSecretKey())
                .setIssuedAt(new Date())
                .setSubject(id)
                .claim("type", type)
                .compact();
    }

    private String encodingSecretKey(){
        return Base64.getEncoder().encodeToString(secretKey.getBytes());
    }
}
