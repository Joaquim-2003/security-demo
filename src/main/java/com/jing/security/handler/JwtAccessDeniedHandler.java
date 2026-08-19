package com.jing.security.handler;

import com.jing.security.common.enums.ResultCode;
import com.jing.security.common.result.Result;
import com.jing.security.util.ResponseUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * JWT认证成功，但没有权限
 */
@Component
@RequiredArgsConstructor
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

    private final ResponseUtil responseUtil;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException)
        throws IOException{

        Result<Void> result = Result.error(ResultCode.FORBIDDEN);

        responseUtil.writeJson(response, HttpServletResponse.SC_FORBIDDEN, result);
    }
}
