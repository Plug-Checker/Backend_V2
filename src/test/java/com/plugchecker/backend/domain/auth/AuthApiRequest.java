package com.plugchecker.backend.domain.auth;

import com.plugchecker.backend.ApiTest;
import com.plugchecker.backend.domain.auth.dto.request.EmailRequest;
import com.plugchecker.backend.domain.auth.dto.request.RefreshTokenRequest;
import com.plugchecker.backend.domain.auth.dto.request.SignInRequest;
import com.plugchecker.backend.domain.auth.dto.request.SignUpRequest;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

public class AuthApiRequest extends ApiTest {

    protected ResultActions requestCheckNumber(EmailRequest request) throws Exception {
        return requestMvc(post("/email"), request);
    }

    protected ResultActions requestSignUp(SignUpRequest request) throws Exception {
        return requestMvc(post("/sign-up"), request);
    }

    protected ResultActions requestLogin(SignInRequest request) throws Exception {
        return requestMvc(post("/login"), request);
    }

    protected ResultActions requestTokenRefresh(RefreshTokenRequest request) throws Exception {
        return requestMvc(post("/token-refresh"), request);
    }

}
