package com.plugchecker.backend.domain.plug.dto.response;

import com.plugchecker.backend.domain.plug.domain.Plug;
import lombok.*;

@Getter
@Builder(access = AccessLevel.PRIVATE)
public class PlugIdNameResponse {

    private final int id;
    private final String name;

    public static PlugIdNameResponse from(Plug plug) {
        return PlugIdNameResponse.builder()
                .id(plug.getId())
                .name(plug.getName())
                .build();
    }
}
