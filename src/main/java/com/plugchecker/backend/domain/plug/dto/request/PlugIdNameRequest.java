package com.plugchecker.backend.domain.plug.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PlugIdNameRequest {
    @NotNull
    private int id;
    @NotNull
    private String name;
}
