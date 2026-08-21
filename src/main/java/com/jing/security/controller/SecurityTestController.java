package com.jing.security.controller;

import com.jing.security.common.result.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Spring Security + JWT 请求链路
 */
@RequiredArgsConstructor
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

    /**
     * 测试add权限
     */
    @PostMapping("/add")
    @PreAuthorize("hasAuthority('user:add')")
    public Result<Void> add() {
        return Result.success();
    }

    /**
     * 测试list权限
     */
    @GetMapping("/list")
    @PreAuthorize("hasAuthority('user:list')")
    public Result<Void> list() {
        return Result.success();
    }
}
