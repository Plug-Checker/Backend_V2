package com.plugchecker.backend.domain.security;

import com.plugchecker.backend.IntegrationTest;
import com.plugchecker.backend.domain.auth.domain.User;
import com.plugchecker.backend.domain.auth.domain.UserRepository;
import com.plugchecker.backend.global.security.details.CustomUserDetailsService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class CustomUserDetailsServiceTest extends IntegrationTest {

    @Autowired
    private CustomUserDetailsService customUserDetailsService;
    @Autowired
    private UserRepository userRepository;

    @Test
    void loadUserByUsername_Test() {
        // given
        String id = "testUserId";
        String password = "testUserPassword";
        String email = "xxxxxxx@gmail.com";
        User user = User.builder()
                .id(id)
                .password(password)
                .email(email)
                .build();
        userRepository.save(user);

        // when
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(id);

        // then
        Assertions.assertEquals(id, userDetails.getUsername());
        Assertions.assertEquals(password, userDetails.getPassword());
        Assertions.assertTrue(userDetails.isAccountNonExpired());
        Assertions.assertTrue(userDetails.isAccountNonLocked());
        Assertions.assertTrue(userDetails.isCredentialsNonExpired());
        Assertions.assertTrue(userDetails.isEnabled());
    }

    @Test
    void loadUserByUsername_UsernameNotFoundException_Test() {
        // given
        String id = "notExistsUserId";

        // when, then
        Assertions.assertThrows(UsernameNotFoundException.class, ()-> customUserDetailsService.loadUserByUsername(id));
    }
}
