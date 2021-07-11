package com.plugchecker.backend.global.security.details;

import com.plugchecker.backend.domain.auth.domain.User;
import com.plugchecker.backend.domain.auth.domain.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {

        Optional<User> user = userRepository.findById(id);
        if(user.isPresent()) {
            return new CustomUserDetails(user.get());
        }
        throw new UsernameNotFoundException(id);

    }
}
