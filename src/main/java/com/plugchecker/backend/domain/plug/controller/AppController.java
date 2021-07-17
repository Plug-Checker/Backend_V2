package com.plugchecker.backend.domain.plug.controller;

import com.plugchecker.backend.domain.plug.dto.request.PlugIdNameRequest;
import com.plugchecker.backend.domain.plug.dto.request.PlugIdRequest;
import com.plugchecker.backend.domain.plug.dto.request.PlugNameRequest;
import com.plugchecker.backend.domain.plug.dto.response.PlugAllResponse;
import com.plugchecker.backend.domain.plug.dto.response.PlugIdNameResponse;
import com.plugchecker.backend.domain.plug.dto.response.PlugIdResponse;
import com.plugchecker.backend.domain.plug.service.AppService;
import com.plugchecker.backend.global.security.AuthenticationFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/plug")
public class AppController {

    private final AppService plugService;
    private final AuthenticationFacade authenticationFacade;

    @GetMapping
    public List<PlugAllResponse> getPlugAll() {
        return plugService.getPlugAll(authenticationFacade.getUser());
    }

    @GetMapping("/on")
    public List<PlugIdNameResponse> getPlugOn() {
        return plugService.getPlugOn(authenticationFacade.getUser());
    }

    @GetMapping("/off")
    public List<PlugIdNameResponse> getPlugOff() {
        return plugService.getPlugOff(authenticationFacade.getUser());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PlugIdResponse registPlug(@Valid @RequestBody PlugNameRequest request) {
        return plugService.registPlug(request.getName(), authenticationFacade.getUser());
    }

    @PatchMapping
    public void changePlug(@Valid @RequestBody PlugIdNameRequest request) {
        plugService.changePlug(request, authenticationFacade.getUser());
    }

    @DeleteMapping
    public void deletePlug(@Valid @RequestBody PlugIdRequest request) {
        plugService.deletePlug(request.getId(), authenticationFacade.getUser());
    }
}
