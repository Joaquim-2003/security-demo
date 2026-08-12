package com.jing.security;

import com.jing.security.config.properties.JwtProperties;
import com.jing.security.jwt.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Slf4j
public class JwtUtilTest {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private JwtProperties jwtProperties;

    /**
     * 测试生成token
     */
    @Test
    void testGenerateToken() {
        String token = jwtUtil.generateToken(1L, "admin");

        assertNotNull(token);
        log.info("生成的token: {}", token);
    }

    /**
     * 测试解析token
     */
    @Test
    void testParseToken() {
        String token = jwtUtil.generateToken(1L, "admin");
        Claims claims = jwtUtil.parseToken(token);

        assertEquals("1", claims.getSubject());
        assertEquals("admin", claims.get("username",String.class));
        assertNotNull(claims.getIssuedAt());
        assertNotNull(claims.getExpiration());

        log.info("解析出的token: {}", claims);
    }

    /**
     * 测试验证token(正确)
     */
    @Test
    void testValidateTokenTrue() {
        String token = jwtUtil.generateToken(1L, "admin");

        boolean validateToken = jwtUtil.validateToken(token);

        assertTrue(validateToken);
        log.info("测试token-1是否正确: {}", validateToken);
    }

    /**
     * 测试验证token(错误)
     */
    @Test
    void testValidateTokenFalse() {
        String falseToken = jwtUtil.generateToken(1L, "admin") + "a2c";

        boolean validateToken = jwtUtil.validateToken(falseToken);

        assertFalse(validateToken);
        log.info("测试token-2是否正确: {}", validateToken);

    }

    /**
     * 测试验证token超出时间(错误)
     */
    @Test
    void testExpiredToken() throws InterruptedException {
        Long originalExpireTime = jwtProperties.getExpire();

        try {
            jwtProperties.setExpire(1000L);

            String token = jwtUtil.generateToken(1L, "admin");

            Thread.sleep(1500);

            boolean validateToken = jwtUtil.validateToken(token);

            log.info("测试token-3是否正确: {}", validateToken);
        }finally {
            jwtProperties.setExpire(originalExpireTime);
        }
    }
}
