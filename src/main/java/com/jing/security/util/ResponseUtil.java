package com.jing.security.util;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
@RequiredArgsConstructor
public class ResponseUtil {

    private final ObjectMapper objectMapper;

    public void writeJson(HttpServletResponse response, int status, Object result)
        throws IOException {
        //设置状态码
        response.setStatus(status);
        //返回数据格式为application/json
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        //设置字符编码为UTF-8
        response.setCharacterEncoding(StandardCharsets.UTF_8);

        //把 Java 对象序列化成 JSON 格式的字符串
        String json = objectMapper.writeValueAsString(result);

        response.getWriter().write(json);

    }
}
