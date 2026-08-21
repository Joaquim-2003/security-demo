package com.jing.security.config;

import com.jing.security.security.filter.JwtAuthenticationFilter;
import com.jing.security.handler.JwtAccessDeniedHandler;
import com.jing.security.handler.JwtAuthenticationEntryPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * 配置 Spring Security 的认证方式和接口访问权限。
 */
@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // 禁用csrf防护
            .csrf((csrf) -> csrf.disable())
            // 设置为无状态认证，不使用 Session 保存认证信息
            .sessionManagement((session) -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            // 禁用httpBasic认证
            .httpBasic(httpBasic -> httpBasic.disable())
            // 禁用formLogin认证
            .formLogin(formLogin -> formLogin.disable())
            // 放行 /test/** 路径，无需登录认证即可访问
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/test/**").permitAll().anyRequest().authenticated())
            // 在传统用户名密码认证 Filter 的位置之前运行
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
            // 专门设置认证失败处理器
            .exceptionHandling(exception -> exception
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .accessDeniedHandler(jwtAccessDeniedHandler));

        return http.build();
    }
}
