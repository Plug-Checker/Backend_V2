package com.plugchecker.backend.domain.auth.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SignInRequest {
    @NotNull
    @Size(min=5)
    private String id;

    @NotNull
    @Size(min=5)
    private String password;
}
