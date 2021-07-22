package com.plugchecker.backend.domain.plug.dto.response;

import com.plugchecker.backend.domain.plug.domain.Plug;
import lombok.*;

@Getter
@Builder(access = AccessLevel.PRIVATE)
public class PlugAllResponse {

    private final int id;
    private final String name;
    private final boolean electricity;

    public static PlugAllResponse from(Plug plug) {
        return PlugAllResponse.builder()
                .id(plug.getId())
                .name(plug.getName())
                .electricity(plug.isElectricity())
                .build();
    }
}
