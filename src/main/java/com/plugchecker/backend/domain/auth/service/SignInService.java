package com.plugchecker.backend.domain.auth.service;

import com.plugchecker.backend.domain.auth.domain.RefreshTokenRepository;
import com.plugchecker.backend.domain.auth.dto.request.SignInRequest;
import com.plugchecker.backend.domain.auth.dto.response.TokenResponse;
import com.plugchecker.backend.global.error.exception.InvalidTokenException;
import com.plugchecker.backend.global.error.exception.NotFoundException;
import com.plugchecker.backend.global.security.JwtTokenProvider;
import com.plugchecker.backend.global.security.details.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SignInService {

    private final CustomUserDetailsService userDetailsService;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    @Value("${auth.jwt.refresh}")
    private Long refreshLifespan;

    public TokenResponse signIn(SignInRequest request) {
        String id = request.getId();
        UserDetails userDetails = userDetailsService.loadUserByUsername(id);
        if(!passwordEncoder.matches(request.getPassword(), userDetails.getPassword())) {
            throw new NotFoundException(request.getId());
        }
        String accessToken = jwtTokenProvider.generateAccessToken(id);
        String refreshToken = jwtTokenProvider.generateRefreshToken(id);
        refreshTokenRepository.saveRefreshToken(refreshToken, refreshLifespan);

        return new TokenResponse(accessToken,refreshToken);
    }

    public TokenResponse tokenRefresh(String refreshToken) {
        refreshTokenRepository.findByRefreshToken(refreshToken)
                .orElseThrow(InvalidTokenException::new);
        if(!jwtTokenProvider.isRefreshToken(refreshToken)) {
            throw new InvalidTokenException();
        }
        String id = jwtTokenProvider.getId(refreshToken);

        String newAccessToken = jwtTokenProvider.generateAccessToken(id);
        String newRefreshToken = jwtTokenProvider.generateRefreshToken(id);
        refreshTokenRepository.saveRefreshToken(refreshToken, refreshLifespan);

        return new TokenResponse(newAccessToken,newRefreshToken);
    }
}
