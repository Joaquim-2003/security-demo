package com.jing.security.security.filter;

import com.jing.security.config.properties.JwtProperties;
import com.jing.security.handler.JwtAuthenticationEntryPoint;
import com.jing.security.util.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
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

    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException {

        String authHeader = request.getHeader(jwtProperties.getHeader());

        // 未携带合法 Bearer 请求头时跳过 JWT 认证，交由后续过滤链处理
        if (authHeader == null || !authHeader.startsWith(jwtProperties.getPrefix())) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(jwtProperties.getPrefix().length());

        try {
            // 解析 JWT 并将用户身份信息保存到 Spring Security 上下文
            Claims claims = jwtUtil.parseToken(token);

            String userId = claims.getSubject();
            String username = claims.get("username", String.class);

            // TODO principal 当前用户是谁；credentials 认证凭证；authorities 当前用户有哪些权限
            UsernamePasswordAuthenticationToken authenticated =
                UsernamePasswordAuthenticationToken.authenticated(username, null, Collections.emptyList());

            // 获取认证信息
            if (SecurityContextHolder.getContext().getAuthentication() == null) {
                SecurityContextHolder.getContext().setAuthentication(authenticated);
            }
        } catch (JwtException | IllegalArgumentException e) {
            // JWT 无效或解析失败，按认证失败处理
            jwtAuthenticationEntryPoint.commence(request, response, new BadCredentialsException(e.getMessage(), e));
            return;
        }

        filterChain.doFilter(request, response);
    }
}