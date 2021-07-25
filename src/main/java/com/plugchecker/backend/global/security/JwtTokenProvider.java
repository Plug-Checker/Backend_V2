package com.plugchecker.backend.global.security;

import com.plugchecker.backend.global.error.exception.InvalidTokenException;
import com.plugchecker.backend.global.security.details.CustomUserDetailsService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    private final CustomUserDetailsService userDetailsService;

    @Value("${auth.jwt.access}")
    private Long accessLifespan;

    @Value("${auth.jwt.refresh}")
    private Long refreshLifespan;

    @Value("${auth.jwt.secret}")
    private String secretKey;

    public String generateAccessToken(String id) {
        return makingToken(id, "access", accessLifespan);
    }

    public String generateRefreshToken(String id) { return makingToken(id, "refresh", refreshLifespan); }

    public boolean isAccessToken(String token){
        return checkTokenType(token, "access");
    }

    public boolean isRefreshToken(String token){
        return checkTokenType(token, "refresh");
    }

    public String getId(String token) {
        try {
            return Jwts.parser().setSigningKey(encodingSecretKey()).parseClaimsJws(token).getBody().getSubject();
        } catch (Exception e) {
            throw new InvalidTokenException();
        }
    }

    public String resolveToken(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if(checkToken(token)) {
            return token.substring(7);
        }
        return null;
    }

    private Boolean checkToken(String token) {
        return token != null && token.startsWith("Bearer ");
    }

    public Authentication getAuthentication(String token) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(getId(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }


    private boolean checkTokenType(String token, String typeKind) {
        try {
            String type = Jwts.parser().setSigningKey(encodingSecretKey()).parseClaimsJws(token).getBody().get("type", String.class);
            return type.equals(typeKind);
        } catch (Exception e) {
            throw new InvalidTokenException();
        }
    }

    private String makingToken(String id, String type, Long time){
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
