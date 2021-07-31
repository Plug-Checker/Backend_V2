package com.plugchecker.backend.domain.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Profile;

import javax.validation.constraints.NotNull;

@Profile("test")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GlobalExceptionHandlerTestDto {
    @NotNull
    private String value;
}
