package com.plugchecker.backend.domain.plug.service;

import com.plugchecker.backend.domain.plug.domain.Plug;
import com.plugchecker.backend.domain.plug.domain.PlugRepository;
import com.plugchecker.backend.global.error.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HardwareService {

    private final PlugRepository plugRepository;

    public void plugOn(int id) {
        Plug plug = plugRepository.findById(id)
                .orElseThrow(()-> new NotFoundException(id));

        plug.setElectricity(true);
        plugRepository.save(plug);
    }

    public void plugOff(int id) {
        Plug plug = plugRepository.findById(id)
                .orElseThrow(()-> new NotFoundException(id));

        plug.setElectricity(false);
        plugRepository.save(plug);
    }
}
