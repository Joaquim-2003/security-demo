package com.jing.security.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Spring Security + JWT 请求链路
 */
@RestController
@RequestMapping("/security")
public class SecurityTestController {

    /**
     * 测试不带 Authorization请求头
     */
    @PostMapping("/test")
    public String test() {
        return "OK";
    }

    /**
     * 测试携带合法 JWT 后是否能够通过 Spring Security 认证并访问受保护接口
     */
    @PostMapping("/protected")
    public String testProtectedEndpoint() {
        return "OK";
    }
}
