package com.plugchecker.backend.domain.auth;

import com.plugchecker.backend.ApiTest;
import org.springframework.test.web.servlet.ResultActions;

public class AuthApiTest extends ApiTest {
    private ResultActions request() throws Exception {
        return requestMvc(get("/taxi-pot/info"));
    }
}
