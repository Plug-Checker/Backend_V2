package com.plugchecker.backend.domain.security;

import org.springframework.context.annotation.Profile;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Profile("test")
@RestController
public class JwtTokenFilterTestController {

    @GetMapping("/testFilter")
    public JwtTokenFilterTestDto testFilter(){
        String info = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!info.equals("anonymousUser")) {
            return new JwtTokenFilterTestDto(true);
        }
        return new JwtTokenFilterTestDto(false);
    }
}
