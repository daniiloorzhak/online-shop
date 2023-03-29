package ru.oorzhak.gateway.security;

import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.oorzhak.gateway.exception.NoAuthorizationHeader;
import ru.oorzhak.gateway.util.JwtUtil;

import java.io.IOException;
import java.util.Optional;

@Component
public class JwtVerifierFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        try {
            String headerValue = Optional.of(request.getHeader("Authorization")).orElseThrow();
            String jwt = extractJwtFromHeader(headerValue);

        } catch (NoAuthorizationHeader e) {
            throw new RuntimeException(e);
        }

        filterChain.doFilter(request, response);
    }

    private static String extractJwtFromHeader(String headerValue) {
        return null;
    }
}
