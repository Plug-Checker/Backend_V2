package com.plugchecker.backend.domain.auth.service;

import com.plugchecker.backend.domain.auth.domain.*;
import com.plugchecker.backend.domain.auth.dto.request.EmailRequest;
import com.plugchecker.backend.domain.auth.dto.request.SignUpRequest;
import com.plugchecker.backend.domain.auth.dto.response.TokenResponse;
import com.plugchecker.backend.global.error.exception.AlreadyExistException;
import com.plugchecker.backend.global.error.exception.InvalidCertificationNumberException;
import com.plugchecker.backend.global.error.exception.NotFoundException;
import com.plugchecker.backend.global.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SignUpService {

    private final CertificationNumberRepository certificationNumberRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;
    private final EmailSendService emailSendService;
    private final JwtTokenProvider jwtTokenProvider;

    @Value("${auth.jwt.refresh}")
    private Long refreshLifespan;

    public TokenResponse checkNumber(EmailRequest request) {
        String email = request.getEmail();
        String number = request.getNumber();

        CertificationNumber certificationNumber =
                certificationNumberRepository.findByEmail(email)
                .orElseThrow(()-> new NotFoundException(email));

        if(!certificationNumber.getNumber().equals(number)) {
            throw new InvalidCertificationNumberException(number);
        }

        User user = User.builder()
                .id(certificationNumber.getUser_id())
                .password(certificationNumber.getUser_password())
                .email(certificationNumber.getEmail())
                .build();
        userRepository.save(user);

        String id = certificationNumber.getUser_id();
        String accessToken = jwtTokenProvider.generateAccessToken(id);
        String refreshToken = jwtTokenProvider.generateRefreshToken(id);
        refreshTokenRepository.saveRefreshToken(refreshToken, refreshLifespan);

        return new TokenResponse(accessToken,refreshToken);
    }

    public void signUp(SignUpRequest request) {
        String id = request.getId();
        String password = request.getPassword();
        String email = request.getEmail();

        if(userRepository.existsById(id)) {
            throw new AlreadyExistException(id);
        }

        if(userRepository.existsByEmail(email)) {
            throw new AlreadyExistException(email);
        }

        String number = makeCertificationNumber();
        emailSendService.sendCertification(email, number);

        certificationNumberRepository.findByEmail(email)
                .ifPresent(certificationNumberRepository::delete);

        certificationNumberRepository.saveCertificationNumber(email, number, id, passwordEncoder.encode(password));
    }

    private String makeCertificationNumber() {
        return String.valueOf((int)(Math.random()*10000)-1);
    }
}
