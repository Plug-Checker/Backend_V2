package com.plugchecker.backend.domain.plug.dto.response;

import com.plugchecker.backend.domain.plug.domain.Plug;
import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder(access = AccessLevel.PRIVATE)
public class PlugAllResponse {

    private int id;
    private String name;
    private boolean electricity;

    public static PlugAllResponse from(Plug plug) {
        return PlugAllResponse.builder()
                .id(plug.getId())
                .name(plug.getName())
                .electricity(plug.isElectricity())
                .build();
    }
}
