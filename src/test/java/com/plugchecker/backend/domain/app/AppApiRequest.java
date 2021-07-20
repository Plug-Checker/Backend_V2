package com.plugchecker.backend.domain.app;

import com.plugchecker.backend.ApiTest;
import com.plugchecker.backend.domain.plug.dto.request.PlugIdNameRequest;
import com.plugchecker.backend.domain.plug.dto.request.PlugIdRequest;
import com.plugchecker.backend.domain.plug.dto.request.PlugNameRequest;
import com.plugchecker.backend.global.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

public class AppApiRequest extends ApiTest {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    protected ResultActions requestGetPlugAll(String token) throws Exception {
        return requestMvc(get("/plug").header("AUTHORIZATION", "Bearer " + token));
    }

    protected ResultActions requestGetPlugOn(String token) throws Exception {
        return requestMvc(get("/plug/on").header("AUTHORIZATION", "Bearer " + token));
    }

    protected ResultActions requestGetPlugOff(String token) throws Exception {
        return requestMvc(get("/plug/off").header("AUTHORIZATION", "Bearer " + token));
    }

    protected ResultActions requestRegistPlug(PlugNameRequest request, String token) throws Exception {
        return requestMvc(post("/plug").header("AUTHORIZATION", "Bearer " + token), request);
    }

    protected ResultActions requestChangePlug(PlugIdNameRequest request, String token) throws Exception {
        return requestMvc(patch("/plug").header("AUTHORIZATION", "Bearer " + token), request);
    }

    protected ResultActions requestDeletePlug(PlugIdRequest request, String token) throws Exception {
        return requestMvc(delete("/plug").header("AUTHORIZATION", "Bearer " + token), request);
    }

    protected String makeAccessToken(String user){
        return jwtTokenProvider.generateAccessToken(user);
    }

    protected String makeRefreshToken(String user){
        return jwtTokenProvider.generateRefreshToken(user);
    }
}
