package com.fullDetailed.fullDetailedDemo.config;

import com.fullDetailed.fullDetailedDemo.config.securityServices.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
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
import com.fullDetailed.fullDetailedDemo.config.securityServices.JwtUtil;

import lombok.RequiredArgsConstructor;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

  private final CustomUserServiceDetails userServiceDetails;
  private final JwtUtil jwtUtil;


  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http)throws Exception{
    JwtAuthenticationFilter jwtFilter=new JwtAuthenticationFilter(jwtUtil,userServiceDetails);
    return http
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .csrf(csrf->csrf.disable())
            .sessionManagement(sm->sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth->auth
                    .requestMatchers("/api/auth/**").permitAll()
                    .requestMatchers("/api/v1/cases/**").authenticated()
                    .requestMatchers("/api/v1/notifications").authenticated()
                    .requestMatchers("/api/v1/judges/**").hasRole("JUDGE")
                    .requestMatchers("/api/v1/lawyer").hasRole("LAWYER")
                    .requestMatchers(
                            "/api/v1/admin/users/**"
                                    ,"/api/v1/admin/cases"
                    ).hasRole("ADMIN")
                    .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                    .requestMatchers("/ws/**").permitAll()
                    .requestMatchers(
                            "/v3/api-docs/**",
                            "/swagger-ui/**",
                            "/swagger-ui.html",
                            "/swagger-resources/**",
                            "/webjars/**"
                    ).permitAll()
                    .anyRequest().authenticated()
            )
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
            .build();
  }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("*"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;

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
