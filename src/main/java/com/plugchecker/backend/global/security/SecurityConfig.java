package com.plugchecker.backend.global.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter implements WebMvcConfigurer {

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
                        "/swagger-resources/**").permitAll()
                .antMatchers("/email",
                        "/sign-up",
                        "/login",
                        "/token-refresh").permitAll()
                .antMatchers(HttpMethod.POST, "/plug/**").permitAll()
                .antMatchers(HttpMethod.GET, "/plug*", "/plug/*").hasAnyRole("USER")
                .antMatchers("/plug").hasAnyRole("USER")
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
