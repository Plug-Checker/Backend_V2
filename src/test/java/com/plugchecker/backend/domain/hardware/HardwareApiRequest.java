package com.plugchecker.backend.domain.hardware;

import com.plugchecker.backend.ApiTest;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

public class HardwareApiRequest extends ApiTest {

    protected ResultActions requestPlugOn(int id) throws Exception {
        return requestMvc(post("/plug/on/" + id));
    }

    protected ResultActions requestPlugOff(int id) throws Exception {
        return requestMvc(post("/plug/off/"+id));
    }
}
