package com.jing.security.security.filter;

import com.jing.security.config.properties.JwtProperties;
import com.jing.security.handler.JwtAuthenticationEntryPoint;
import com.jing.security.security.model.LoginUser;
import com.jing.security.security.service.SecurityUserService;
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

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtProperties jwtProperties;

    private final JwtUtil jwtUtil;

    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    private final SecurityUserService securityUserService;

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

            Long userId = Long.valueOf(claims.getSubject());

            LoginUser loginUser = securityUserService.loadLoginUser(userId);

            // 创建已认证的 Authentication：
            //                      principal 表示当前用户;
            //                      credentials：JWT 场景下无需密码;
            //                      authorities 表示当前用户拥有的权限;
            UsernamePasswordAuthenticationToken authenticated =
                    UsernamePasswordAuthenticationToken.authenticated(
                        loginUser,
                        null,
                        loginUser.getAuthorities()
                    );

            if (SecurityContextHolder.getContext().getAuthentication() == null) {
                // 将认证信息保存到 Spring Security 上下文，供当前请求后续的权限校验使用
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