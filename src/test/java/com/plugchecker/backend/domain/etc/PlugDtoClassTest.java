package com.plugchecker.backend.domain.etc;

import com.plugchecker.backend.IntegrationTest;
import com.plugchecker.backend.domain.plug.domain.Plug;
import com.plugchecker.backend.domain.plug.dto.request.PlugIdNameRequest;
import com.plugchecker.backend.domain.plug.dto.request.PlugIdRequest;
import com.plugchecker.backend.domain.plug.dto.request.PlugNameRequest;
import com.plugchecker.backend.domain.plug.dto.response.PlugAllResponse;
import com.plugchecker.backend.domain.plug.dto.response.PlugIdNameResponse;
import com.plugchecker.backend.domain.plug.dto.response.PlugIdResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class PlugDtoClassTest extends IntegrationTest {

    @Test
    @DisplayName("plug request dto 테스트")
    void plugRequestDto_Test() {
        // PlugIdNameRequest
        PlugIdNameRequest plugIdNameRequest1 = new PlugIdNameRequest();
        Assertions.assertNotNull(plugIdNameRequest1);
        PlugIdNameRequest plugIdNameRequest2 = new PlugIdNameRequest(1, "testName");
        Assertions.assertEquals(1, plugIdNameRequest2.getId());
        Assertions.assertEquals("testName", plugIdNameRequest2.getName());

        //PlugIdRequest
        PlugIdRequest plugIdRequest1 = new PlugIdRequest();
        Assertions.assertNotNull(plugIdRequest1);
        PlugIdRequest plugIdRequest2 = new PlugIdRequest(1);
        Assertions.assertEquals(1, plugIdRequest2.getId());

        // PlugNameRequest
        PlugNameRequest plugNameRequest1 = new PlugNameRequest();
        Assertions.assertNotNull(plugNameRequest1);
        PlugNameRequest plugNameRequest2 = new PlugNameRequest("testName");
        Assertions.assertEquals("testName", plugNameRequest2.getName());
    }

    @Test
    @DisplayName("plug response dto 테스트")
    void plugResponseDto_Test() {

        Plug plug = Plug.builder()
                .id(1)
                .name("testName")
                .electricity(true)
                .build();

        // PlugAllResponse
        PlugAllResponse plugAllResponse = PlugAllResponse.from(plug);
        Assertions.assertEquals(plugAllResponse.getId(), plug.getId());
        Assertions.assertEquals(plugAllResponse.getName(), plug.getName());
        Assertions.assertEquals(plugAllResponse.isElectricity(), plug.isElectricity());

        // PlugIdNameResponse
        PlugIdNameResponse plugIdNameResponse = PlugIdNameResponse.from(plug);
        Assertions.assertEquals(plugIdNameResponse.getId(), plug.getId());
        Assertions.assertEquals(plugIdNameResponse.getName(), plug.getName());

        // PlugIdResponse
        PlugIdResponse plugIdResponse = new PlugIdResponse(1);
        Assertions.assertEquals(plugIdResponse.getId(), 1);
    }
}
