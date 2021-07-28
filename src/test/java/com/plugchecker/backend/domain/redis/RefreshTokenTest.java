package com.plugchecker.backend.domain.redis;

import com.plugchecker.backend.IntegrationTest;
import com.plugchecker.backend.domain.auth.domain.CertificationNumber;
import com.plugchecker.backend.domain.auth.domain.RefreshToken;
import com.plugchecker.backend.domain.auth.domain.RefreshTokenRepository;
import com.plugchecker.backend.global.error.exception.NotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

public class RefreshTokenTest extends IntegrationTest {
    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @BeforeEach
    void clear() {
        refreshTokenRepository.deleteAll();
    }

    @Test
    void makeRefreshToken_Test() {
        // when
        RefreshToken refreshToken1 = new RefreshToken();
        RefreshToken refreshToken2 = new RefreshToken(null, "refreshToken1", 3000L);
        refreshTokenRepository.save(refreshToken2);
        RefreshToken refreshToken3 = RefreshToken.builder()
                .refreshToken("refreshToken2")
                .exp(3000L)
                .build();
        refreshTokenRepository.save(refreshToken3);

        // then
        RefreshToken findRefreshToken1 = refreshTokenRepository.findByRefreshToken("refreshToken1")
                .orElseThrow(()-> new NotFoundException("refreshToken1"));
        RefreshToken findRefreshToken2 = refreshTokenRepository.findByRefreshToken("refreshToken2")
                .orElseThrow(()-> new NotFoundException("refreshToken2"));

        Assertions.assertNotNull(refreshToken1);

        Assertions.assertNotNull(findRefreshToken1.getId());
        Assertions.assertEquals(refreshToken2.getRefreshToken(), findRefreshToken1.getRefreshToken());
        Assertions.assertEquals(refreshToken2.getExp(), findRefreshToken1.getExp());

        Assertions.assertNotNull(findRefreshToken2.getId());
        Assertions.assertEquals(refreshToken3.getRefreshToken(), findRefreshToken2.getRefreshToken());
        Assertions.assertEquals(refreshToken3.getExp(), findRefreshToken2.getExp());
    }

    @Test
    void updateExp_Test(){
        // given
        RefreshToken refreshToken = RefreshToken.builder()
                .refreshToken("refreshToken")
                .exp(3000L)
                .build();

        // when
        refreshToken.updateExp(40000L);
        refreshTokenRepository.save(refreshToken);

        // then
        RefreshToken findRefreshToken = refreshTokenRepository.findByRefreshToken("refreshToken")
                .orElseThrow(()-> new NotFoundException("refreshToken"));

        Assertions.assertEquals(refreshToken.getExp(), findRefreshToken.getExp());
    }

    @Test
    void timeExtinction_Test() throws InterruptedException {
        // given
        RefreshToken refreshToken = RefreshToken.builder()
                .refreshToken("refreshToken")
                .exp(1L)
                .build();
        refreshTokenRepository.save(refreshToken);

        // when
        TimeUnit.SECONDS.sleep(refreshToken.getExp()+10L);

        // then
        Optional<RefreshToken> findRefreshToken = refreshTokenRepository.findByRefreshToken("refreshToken") ;

        Assertions.assertTrue(findRefreshToken.isEmpty());

    }

    @Test
    void saveRefreshToken_Test() {
        // given
        String refreshToken = "refreshToken";
        Long exp = 1000L;

        // when
        refreshTokenRepository.saveRefreshToken(refreshToken, exp);

        // then
        RefreshToken findRefreshToken = refreshTokenRepository.findByRefreshToken("refreshToken")
                .orElseThrow(()-> new NotFoundException("refreshToken"));

        Assertions.assertNotNull(findRefreshToken.getId());
        Assertions.assertEquals(refreshToken, findRefreshToken.getRefreshToken());
        Assertions.assertEquals(exp, findRefreshToken.getExp());
    }
}
