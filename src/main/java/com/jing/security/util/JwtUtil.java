package com.jing.security.util;

import com.jing.security.config.properties.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtUtil {

    private final JwtProperties jwtProperties;

    /**
     * 生成token
     *
     * @param userId 用户id
     * @param username 用户名
     * @return JWT字符串
     */
    public String generateToken(Long userId, String username) {

        // 过期时间
        Date expire = new Date(System.currentTimeMillis() + jwtProperties.getExpire());

        return Jwts.builder()
                .subject(String.valueOf(userId))
                .claim("username", username)
                .issuedAt(new Date())
                .expiration(expire)
                .signWith(secretKey())
                .compact();
    }

    /**
     * 解析token
     *
     * @param token Jwt字符串
     * @return JWT载荷
     */
    public Claims parseToken(String token) {
        return Jwts.parser()
                .verifyWith(secretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /**
     * 验证token
     *
     * @param token Jwt字符串
     * @return 验证结果
     */
    public boolean validateToken(String token) {
        try {
            parseToken(token);
            return true;
        }catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    /**
     *
     * 根据HMAC算法的要求转化为SecretKey密钥对象
     *
     * @return 加密后的SecretKey
     */
    private SecretKey secretKey() {
        return Keys.hmacShaKeyFor(jwtProperties.getSecretKey().getBytes(StandardCharsets.UTF_8));
    }
}
