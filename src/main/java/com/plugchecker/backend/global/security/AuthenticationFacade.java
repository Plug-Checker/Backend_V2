package com.plugchecker.backend.global.security;

import com.plugchecker.backend.domain.auth.domain.User;
import com.plugchecker.backend.global.security.details.CustomUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationFacade {

    public Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    public User getUser() {
        Authentication auth = getAuthentication();
        return ((CustomUserDetails) auth.getPrincipal()).getUser();
    }

}
