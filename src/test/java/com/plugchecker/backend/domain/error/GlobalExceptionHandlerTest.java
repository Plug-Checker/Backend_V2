package com.plugchecker.backend.domain.error;

import com.plugchecker.backend.ApiTest;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
public class GlobalExceptionHandlerTest extends ApiTest {

    @Test
    void catchMethodArgumentNotValidException_Test() throws Exception {
        // given
        GlobalExceptionHandlerTestDto request = new GlobalExceptionHandlerTestDto(null);

        // when
        ResultActions resultActions = requestMvc(post("/testMethodArgumentNotValidException"), request);

        // then
        resultActions.andExpect(status().is4xxClientError())
                .andDo(print());
    }

    @Test
    void catchGlobalException_Test() throws Exception {
        // when
        ResultActions resultActions = requestMvc(get("/testGlobalException"));

        // then
        resultActions.andExpect(status().is4xxClientError())
                .andDo(print());
    }

    @Test
    void catchException_Test() throws Exception {
        // when
        ResultActions resultActions = requestMvc(get("/testException"));

        // then
        resultActions.andExpect(status().is5xxServerError())
                .andDo(print());
    }
}
