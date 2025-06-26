package com.ecom.user_service.service;

import com.ecom.user_service.entity.User;
import com.ecom.user_service.repository.UserRepository;
import com.ecom.user_service.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");
        String token = null;
        String username = null;

        // Check for Bearer token in header
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
            username = jwtUtil.extractUsername(token);
        }

        // Validate token and set authentication in SecurityContext
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            Optional<User> optionalUser = userRepository.findByUsername(username);

            if (optionalUser.isPresent() && jwtUtil.validateToken(token, username)) {
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(username, null, List.of());

                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        // Continue filter chain
        filterChain.doFilter(request, response);
    }
}
