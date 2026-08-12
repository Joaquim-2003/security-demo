package com.jing.security.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * JWT配置文件特性
 */
@Component
@ConfigurationProperties(prefix = "jwt")
@Data
public class JwtProperties {

    /**
     * JWT密钥
     */
    private String secretKey;

    /**
     * 过期时间（毫秒）
     */
    private Long expire;

    /**
     *  token请求头名称
     */
    private String header;

    /**
     *  token前缀
     */
    private String prefix;
}
