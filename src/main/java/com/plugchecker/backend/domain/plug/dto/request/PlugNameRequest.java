package com.plugchecker.backend.domain.plug.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PlugNameRequest {
    @NotNull
    private String name;
}
