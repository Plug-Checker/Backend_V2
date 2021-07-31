package com.plugchecker.backend.domain.etc;

import com.plugchecker.backend.global.security.JwtConfigure;
import com.plugchecker.backend.global.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Profile("test")
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class TestSecurityConfig extends WebSecurityConfigurerAdapter implements WebMvcConfigurer {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .sessionManagement().disable()
                .cors().disable()
                .formLogin().disable()
                .authorizeRequests()
                .antMatchers("/swagger-ui/*",
                        "/swagger-ui/**",
                        "/v2/api-docs",
                        "/swagger-resources/**",
                        "/test*").permitAll()
                .antMatchers("/email",
                        "/sign-up",
                        "/login",
                        "/token-refresh").permitAll()
                .antMatchers(HttpMethod.POST, "/plug/**").permitAll()
                .antMatchers(HttpMethod.GET, "/plug", "/plug/*").hasAuthority("USER")
                .antMatchers("/plug").hasAuthority("USER")
                .anyRequest().authenticated()
                .and().apply(new JwtConfigure(jwtTokenProvider));

    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("*");
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
