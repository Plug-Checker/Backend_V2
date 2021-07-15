package com.plugchecker.backend.domain.hardware;

import com.plugchecker.backend.domain.plug.domain.Plug;
import com.plugchecker.backend.domain.plug.domain.PlugRepository;
import com.plugchecker.backend.global.error.exception.NotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class HardwareApiTest extends HardwareApiRequest {

    @Autowired
    private PlugRepository plugRepository;

    @Test
    @DisplayName("멀티탭 켜짐 요청하기")
    void plugOn() throws Exception {
        // given
        Plug plug = Plug.builder()
                .name("테스트 멀티탭")
                .build();
        plugRepository.save(plug);
        int id = plug.getId();

        // when
        ResultActions resultActions = requestPlugOn(id);

        // then
        resultActions.andExpect(status().isOk())
                .andDo(print());

        Plug newPlug = plugRepository.findById(id)
                .orElseThrow(()-> new NotFoundException(id));

        Assertions.assertTrue(newPlug.isElectricity());
    }

    @Test
    @DisplayName("멀티탭 켜짐 요청하기")
    void plugOff() throws Exception {
        // given
        Plug plug = Plug.builder()
                .name("테스트 멀티탭")
                .build();
        plugRepository.save(plug);
        int id = plug.getId();

        // when
        ResultActions resultActions = requestPlugOff(id);

        // then
        resultActions.andExpect(status().isOk())
                .andDo(print());

        Plug newPlug = plugRepository.findById(id)
                .orElseThrow(()-> new NotFoundException(id));

        Assertions.assertFalse(newPlug.isElectricity());
    }
}
