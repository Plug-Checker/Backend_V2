package com.plugchecker.backend.domain.security;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.context.annotation.Profile;

@Profile("test")
@Getter
@AllArgsConstructor
public class JwtTokenFilterTestDto {
    private final boolean token;
}
