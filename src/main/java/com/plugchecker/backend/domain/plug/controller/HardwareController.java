package com.plugchecker.backend.domain.plug.controller;

import com.plugchecker.backend.domain.plug.service.HardwareService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/plug")
public class HardwareController {

    private final HardwareService hardwareService;

    @PostMapping("/on/{id}")
    public void plugOn(@PathVariable("id") int id) {
        hardwareService.plugOn(id);
    }

    @PostMapping("/off/{id}")
    public void plugOff(@PathVariable("id") int id) {
        hardwareService.plugOff(id);
    }
}
