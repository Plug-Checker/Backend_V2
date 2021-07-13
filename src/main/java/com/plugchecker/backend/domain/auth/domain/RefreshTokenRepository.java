package com.plugchecker.backend.domain.auth.domain;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByRefreshToken(String refreshToken);

    default void saveRefreshToken(String token, Long refreshLifespan) {
        RefreshToken refreshToken = RefreshToken.builder()
                .refreshToken(token)
                .exp(refreshLifespan)
                .build();
        this.save(refreshToken);
    }
}
