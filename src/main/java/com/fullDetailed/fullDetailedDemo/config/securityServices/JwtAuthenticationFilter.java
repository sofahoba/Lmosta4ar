package com.fullDetailed.fullDetailedDemo.config.securityServices;

import java.io.IOException;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter{
    private final JwtUtill jwtUtil;
    private final CustomUserServiceDetails userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String header = request.getHeader("Authorization");
        String token = null;
        if (StringUtils.hasText(header) && header.startsWith("Bearer ")) {
            token = header.substring(7);
        }

        if (token != null && jwtUtil.validateToken(token)
                && SecurityContextHolder.getContext().getAuthentication() == null) {

            String username = jwtUtil.extractUsername(token);
            var userDetails = userDetailsService.loadUserByUsername(username);

            var roles = jwtUtil.extractRoles(token);
            var authorities = roles.stream()
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());

            UsernamePasswordAuthenticationToken auth =
                    new UsernamePasswordAuthenticationToken(userDetails, null, authorities);
            auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(auth);
        }

        filterChain.doFilter(request, response);
    }
}
