package com.jing.security.config;

import com.jing.security.filter.JwtAuthenticationFilter;
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

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf((csrf) -> csrf.disable()) // 禁用csrf防护
            .sessionManagement((session) -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)) //设置为无状态认证，不使用 Session 保存认证信息
            .httpBasic(httpBasic -> httpBasic.disable()) //禁用httpBasic认证
            .formLogin(formLogin -> formLogin.disable()) //禁用formLogin认证
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/test/**").permitAll().anyRequest().authenticated())//放行 /test/** 路径，无需登录认证即可访问
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);//在传统用户名密码认证 Filter 的位置之前运行

        return http.build();
    }
}
