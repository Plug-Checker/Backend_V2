package com.plugchecker.backend.domain.hardware;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class HardwareApiExceptionTest extends HardwareApiRequest {

    @Test
    @DisplayName("멀티탭 켜짐 요청하기_NotFoundException")
    void plugOn_NotFoundException() throws Exception {
        // given
        int id = -1;

        // when
        ResultActions resultActions = requestPlugOn(id);

        // then
        resultActions.andExpect(status().is4xxClientError())
                .andDo(print());
    }

    @Test
    @DisplayName("멀티탭 켜짐 요청하기_NotFoundException")
    void plugOff_NotFoundException() throws Exception {
        // given
        int id = -1;

        // when
        ResultActions resultActions = requestPlugOff(id);

        // then
        resultActions.andExpect(status().is4xxClientError())
                .andDo(print());
    }
}