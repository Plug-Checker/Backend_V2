package com.plugchecker.backend.domain.auth.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SignUpRequest {
    @NotNull
    @Size(min=5)
    private String id;

    @NotNull
    @Size(min=5)
    private String password;

    @NotNull
    @Email
    private String email;
}
