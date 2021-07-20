package com.plugchecker.backend.domain.app;

import com.fasterxml.jackson.core.type.TypeReference;
import com.plugchecker.backend.domain.auth.domain.User;
import com.plugchecker.backend.domain.auth.domain.UserRepository;
import com.plugchecker.backend.domain.plug.domain.Plug;
import com.plugchecker.backend.domain.plug.domain.PlugRepository;
import com.plugchecker.backend.domain.plug.dto.request.PlugIdNameRequest;
import com.plugchecker.backend.domain.plug.dto.request.PlugIdRequest;
import com.plugchecker.backend.domain.plug.dto.request.PlugNameRequest;
import com.plugchecker.backend.domain.plug.dto.response.PlugAllResponse;
import com.plugchecker.backend.domain.plug.dto.response.PlugIdNameResponse;
import com.plugchecker.backend.domain.plug.dto.response.PlugIdResponse;
import com.plugchecker.backend.global.error.exception.NotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AppApiTest extends AppApiRequest {

    @Autowired
    private PlugRepository plugRepository;
    @Autowired
    private UserRepository userRepository;

    private final String id = "testId";
    private final String password = "testPassword";
    private final String email = "xxxxxxx@gmail.com";

    private User user;

    @BeforeEach
    private void makeUser(){
        User user = User.builder()
                .id(id)
                .password(password)
                .email(email)
                .build();
        userRepository.save(user);
        this.user = user;
    }

    @Test
    @DisplayName("모든 멀티탭 정보 받아오기")
    void getPlugAll() throws Exception {
        // given
        List<PlugAllResponse> databasePlugs = plugRepository.findByUser(user)
                .stream().map(PlugAllResponse::from)
                .collect(Collectors.toList());
        String token = makeAccessToken(id);

        // when
        ResultActions resultActions = requestGetPlugAll(token);

        // then
        MvcResult result = resultActions.andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        List<PlugAllResponse> responses = objectMapper.readValue(
                result.getResponse().getContentAsString(), new TypeReference<List<PlugAllResponse>>() {});

        Assertions.assertEquals(responses.size(), databasePlugs.size());
        for(int i=0; i<responses.size(); i++) {
            PlugAllResponse databasePlug = databasePlugs.get(i);
            PlugAllResponse responsePlug = responses.get(i);
            Assertions.assertEquals(databasePlug.getId(), responsePlug.getId());
            Assertions.assertEquals(databasePlug.getName(), responsePlug.getName());
            Assertions.assertEquals(databasePlug.isElectricity(), responsePlug.isElectricity());
        }
    }


    @Test
    @DisplayName("켜진 멀티탭 정보 받아오기")
    void getPlugOn() throws Exception {
        // given
        List<PlugIdNameResponse> databasePlugs = plugRepository.findByUserAndElectricity(user,true)
                .stream().map(PlugIdNameResponse::from)
                .collect(Collectors.toList());
        String token = makeAccessToken(id);

        // when
        ResultActions resultActions = requestGetPlugOn(token);

        // then
        getOnOffPlugCheck(resultActions, databasePlugs);
    }


    @Test
    @DisplayName("꺼진 멀티탭 정보 받아오기")
    void getPlugOff() throws Exception {
        // given
        List<PlugIdNameResponse> databasePlugs = plugRepository.findByUserAndElectricity(user,false)
                .stream().map(PlugIdNameResponse::from)
                .collect(Collectors.toList());
        String token = makeAccessToken(id);

        // when
        ResultActions resultActions = requestGetPlugOff(token);

        // then
        getOnOffPlugCheck(resultActions, databasePlugs);
    }

    private void getOnOffPlugCheck(ResultActions resultActions, List<PlugIdNameResponse> databasePlugs) throws Exception {
        MvcResult result = resultActions.andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        List<PlugIdNameResponse> responses = objectMapper.readValue(
                result.getResponse().getContentAsString(), new TypeReference<List<PlugIdNameResponse>>() {});

        Assertions.assertEquals(responses.size(), databasePlugs.size());
        for(int i=0; i<responses.size(); i++) {
            PlugIdNameResponse databasePlug = databasePlugs.get(i);
            PlugIdNameResponse responsePlug = responses.get(i);
            Assertions.assertEquals(databasePlug.getId(), responsePlug.getId());
            Assertions.assertEquals(databasePlug.getName(), responsePlug.getName());
        }
    }

    @Test
    @DisplayName("멀티탭 등록하기")
    void registPlug() throws Exception {
        // given
        PlugNameRequest request = new PlugNameRequest("테스트 멀티탭");
        int amount = (int) plugRepository.count();
        String token = makeAccessToken(id);

        // then
        ResultActions resultActions = requestRegistPlug(request, token);

        // when
        MvcResult result = resultActions.andExpect(status().isCreated())
                .andDo(print())
                .andReturn();

        PlugIdResponse response = objectMapper.readValue(
                result.getResponse().getContentAsString(), new TypeReference<PlugIdResponse>() {});

        int id = response.getId();
        Plug plug = plugRepository.findById(id)
                .orElseThrow(()-> new NotFoundException(id));

        Assertions.assertEquals(plug.getName(), request.getName());
        Assertions.assertEquals(amount+1, (int) plugRepository.count());
    }

    @Test
    @DisplayName("멀티탭 이름 변경하기")
    void changePlug() throws Exception {
        // given
        Plug beforePlug = Plug.builder()
                .name("바꾸기 전 이름")
                .user(user)
                .electricity(true)
                .build();
        plugRepository.save(beforePlug);
        PlugIdNameRequest request = new PlugIdNameRequest(beforePlug.getId(), "바꾼 후 이름");
        int beforeAmount = (int) plugRepository.count();
        String token = makeAccessToken(id);

        // when
        ResultActions resultActions = requestChangePlug(request, token);

        // then
        resultActions.andExpect(status().isOk())
                .andDo(print());

        int id = beforePlug.getId();
        Plug afterPlug = plugRepository.findById(id)
                .orElseThrow(()-> new NotFoundException(id));
        int afterAmount = (int) plugRepository.count();

        Assertions.assertEquals(afterPlug.getName(), request.getName());
        Assertions.assertEquals(beforeAmount, afterAmount);
    }

    @Test
    @DisplayName("멀티탭 삭제하기")
    void deletePlug() throws Exception {
        // given
        Plug plug = Plug.builder()
                .name("바꾸기 전 이름")
                .user(user)
                .electricity(true)
                .build();
        plugRepository.save(plug);
        PlugIdRequest request = new PlugIdRequest(plug.getId());
        int beforeAmount = (int) plugRepository.count();
        String token = makeAccessToken(id);

        // when
        ResultActions resultActions = requestDeletePlug(request, token);

        // then
        resultActions.andExpect(status().isOk())
                .andDo(print());

        int afterAmount = (int) plugRepository.count();

        Assertions.assertTrue(plugRepository.findById(plug.getId()).isEmpty());
        Assertions.assertEquals(beforeAmount-1, afterAmount);
    }
}
