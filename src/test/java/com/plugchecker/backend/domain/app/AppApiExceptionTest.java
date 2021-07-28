package com.plugchecker.backend.domain.app;

import com.plugchecker.backend.domain.auth.domain.User;
import com.plugchecker.backend.domain.auth.domain.UserRepository;
import com.plugchecker.backend.domain.plug.domain.Plug;
import com.plugchecker.backend.domain.plug.domain.PlugRepository;
import com.plugchecker.backend.domain.plug.dto.request.PlugIdNameRequest;
import com.plugchecker.backend.domain.plug.dto.request.PlugIdRequest;
import com.plugchecker.backend.domain.plug.dto.request.PlugNameRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AppApiExceptionTest extends AppApiRequest {

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
    @DisplayName("멀티탭 등록하기_AlreadyExistException")
    void registPlug_AlreadyExistException() throws Exception {
        // given
        Plug plug = Plug.builder()
                .name("테스트 멀티탭")
                .user(user)
                .build();
        plugRepository.save(plug);
        PlugNameRequest request = new PlugNameRequest("테스트 멀티탭");

        String token = makeAccessToken(id);

        // when
        ResultActions resultActions = requestRegistPlug(request, token);

        // then
        resultActions.andExpect(status().is4xxClientError())
                .andDo(print());
    }

    @Test
    @DisplayName("멀티탭 변경하기_AlreadyExistException")
    void changePlug_AlreadyExistException() throws Exception {
        // given
        Plug plug1 = Plug.builder()
                .name("테스트 멀티탭1")
                .user(user)
                .build();
        plugRepository.save(plug1);
        Plug plug2 = Plug.builder()
                .name("테스트 멀티탭2")
                .user(user)
                .build();
        plugRepository.save(plug2);
        PlugIdNameRequest request = new PlugIdNameRequest(plug1.getId(), plug2.getName());

        String token = makeAccessToken(id);

        // when
        ResultActions resultActions = requestChangePlug(request, token);

        // then
        resultActions.andExpect(status().is4xxClientError())
                .andDo(print());
    }

    @Test
    @DisplayName("멀티탭 변경하기_NotFoundException")
    void changePlug_NotFoundException() throws Exception {
        // given
        int plugId = -1;
        PlugIdNameRequest request = new PlugIdNameRequest(plugId, "이름이름");

        String token = makeAccessToken(id);

        // when
        ResultActions resultActions = requestChangePlug(request, token);

        // then
        resultActions.andExpect(status().is4xxClientError())
                .andDo(print());

    }

    @Test
    @DisplayName("멀티탭 삭제하기_NotFoundException")
    void deletePlug_NotFoundException() throws Exception {
        // given
        int plugId = -1;
        PlugIdRequest request = new PlugIdRequest(plugId);
        String token = makeAccessToken(id);

        // when
        ResultActions resultActions = requestDeletePlug(request, token);

        // then
        resultActions.andExpect(status().is4xxClientError())
                .andDo(print());
    }

    @Test
    @DisplayName("멀티탭 삭제하기_InvalidInputValueException")
    void deletePlug_InvalidInputValueException() throws Exception {
        // given
        User fakeUser = User.builder()
                .id("fakeUserId")
                .password("fakeuserPassword")
                .email("xxxxxxx@gmail.com")
                .build();
        Plug plug = Plug.builder()
                .name("바꾸기 전 이름")
                .user(fakeUser)
                .electricity(true)
                .build();
        plugRepository.save(plug);
        PlugIdRequest request = new PlugIdRequest(plug.getId());
        String token = makeAccessToken(id);

        // when
        ResultActions resultActions = requestDeletePlug(request, token);

        // then
        resultActions.andExpect(status().is4xxClientError())
                .andDo(print());
    }
}
