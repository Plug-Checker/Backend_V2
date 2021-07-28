package com.plugchecker.backend.domain.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GlobalExceptionHandlerTestDto {
    @NotNull
    private String value;
}
