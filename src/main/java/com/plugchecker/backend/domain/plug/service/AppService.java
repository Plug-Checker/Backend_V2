package com.plugchecker.backend.domain.plug.service;

import com.plugchecker.backend.domain.auth.domain.User;
import com.plugchecker.backend.domain.plug.domain.Plug;
import com.plugchecker.backend.domain.plug.domain.PlugRepository;
import com.plugchecker.backend.domain.plug.dto.request.PlugIdNameRequest;
import com.plugchecker.backend.domain.plug.dto.response.PlugAllResponse;
import com.plugchecker.backend.domain.plug.dto.response.PlugIdNameResponse;
import com.plugchecker.backend.domain.plug.dto.response.PlugIdResponse;
import com.plugchecker.backend.global.error.exception.AlreadyExistException;
import com.plugchecker.backend.global.error.exception.InvalidInputValueException;
import com.plugchecker.backend.global.error.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AppService {

    private final PlugRepository plugRepository;

    public List<PlugAllResponse> getPlugAll(User user) {
        List<Plug> plugs = plugRepository.findByUser(user);
        return plugs.stream()
                .map(PlugAllResponse::from)
                .collect(Collectors.toList());
    }

    public List<PlugIdNameResponse> getPlugOn(User user) {
        List<Plug> plugs = plugRepository.findByUserAndElectricity(user, true);
        return plugs.stream()
                .map(PlugIdNameResponse::from)
                .collect(Collectors.toList());
    }

    public List<PlugIdNameResponse> getPlugOff(User user) {
        List<Plug> plugs = plugRepository.findByUserAndElectricity(user, false);
        return plugs.stream()
                .map(PlugIdNameResponse::from)
                .collect(Collectors.toList());
    }

    public PlugIdResponse registPlug(String name, User user) {
        if(plugRepository.findByNameAndUser(name, user).isPresent()) {
            throw new AlreadyExistException(name);
        }

        Plug plug = Plug.builder()
                .name(name)
                .user(user)
                .build();
        plugRepository.save(plug);
        return new PlugIdResponse(plug.getId());
    }

    public void changePlug(PlugIdNameRequest request, User user) {
        String name = request.getName();
        int id = request.getId();
        if(plugRepository.findByNameAndUser(name, user).isPresent()) {
            throw new AlreadyExistException(name);
        }
        Plug plug = plugRepository.findById(id)
                .orElseThrow(()-> new NotFoundException(id));

        plug.setName(name);
        plugRepository.save(plug);
    }

    public void deletePlug(int id, User user) {
        Plug plug = plugRepository.findById(id)
                .orElseThrow(()-> new NotFoundException(id));

        if(!plug.getUser().getId().equals(user.getId())){
            throw new InvalidInputValueException();
        }
        plugRepository.deleteById(id);
    }
}
