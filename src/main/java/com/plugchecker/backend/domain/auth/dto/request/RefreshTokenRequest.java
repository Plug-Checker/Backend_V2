package com.plugchecker.backend.domain.auth.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RefreshTokenRequest {
    @NotNull
    private String refreshToken;
}
