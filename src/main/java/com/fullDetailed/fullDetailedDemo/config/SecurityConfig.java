package com.fullDetailed.fullDetailedDemo.config;

import com.fullDetailed.fullDetailedDemo.config.securityServices.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.fullDetailed.fullDetailedDemo.config.securityServices.CustomUserServiceDetails;
import com.fullDetailed.fullDetailedDemo.config.securityServices.JwtUtill;

import lombok.RequiredArgsConstructor;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

  private final CustomUserServiceDetails userServiceDetails;
  private final JwtUtill jwtUtill;


  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http)throws Exception{
    JwtAuthenticationFilter jwtFilter=new JwtAuthenticationFilter(jwtUtill,userServiceDetails);
    return http.csrf(csrf->csrf.disable())
            .sessionManagement(sm->sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth->auth
                    .requestMatchers("/api/auth/**").permitAll()
                    .requestMatchers("/api/cases/**").authenticated()
            )
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
            .build();
  }

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
    return config.getAuthenticationManager();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  
}
