package com.saesubam.util;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .headers(headers -> headers
                .frameOptions(frame -> frame.sameOrigin()) // For H2 console
                .cacheControl(cache -> cache.disable())    // Handled by CacheControlInterceptor
            )
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/", "/login", "/register", "/userregister", "/registerUser", "/verify-otp", "/verify-email", "/privacy-policy", "/terms", "/grievance", "/api/auth/**", "/h2-console/**", "/css/**", "/js/**", "/images/**", "/uploads/**").permitAll()
                .anyRequest().permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/?logout=true")
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .deleteCookies("JSESSIONID")
            );

        return http.build();
    }
}
