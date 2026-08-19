package com.jing.security.handler;

import com.jing.security.common.enums.ResultCode;
import com.jing.security.common.result.Result;
import com.jing.security.util.ResponseUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * JWT认证失败
 */
@Component
@RequiredArgsConstructor
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ResponseUtil responseUtil;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
        throws IOException {

        Result<Void> result = Result.error(ResultCode.UNAUTHORIZED);

        responseUtil.writeJson(response, HttpServletResponse.SC_UNAUTHORIZED, result);
    }
}
