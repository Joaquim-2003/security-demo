package com.jing.security.filter;

import com.jing.security.config.properties.JwtProperties;
import com.jing.security.jwt.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtProperties jwtProperties;

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException {

        String authHeader = request.getHeader(jwtProperties.getHeader());

        if (authHeader == null || !authHeader.startsWith(jwtProperties.getPrefix())) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(jwtProperties.getPrefix().length());

        try {
            Claims claims = jwtUtil.parseToken(token);

            String userId = claims.getSubject();
            String username = claims.get("username", String.class);

            // TODO principal 当前用户是谁；credentials 认证凭证；authorities 当前用户有哪些权限
            UsernamePasswordAuthenticationToken authenticated =
                UsernamePasswordAuthenticationToken.authenticated(username, null, Collections.emptyList());

            SecurityContextHolder.getContext().setAuthentication(authenticated);
        } catch (JwtException | IllegalArgumentException e) {
            // TODO 返回异常
        }

        filterChain.doFilter(request, response);
    }
}