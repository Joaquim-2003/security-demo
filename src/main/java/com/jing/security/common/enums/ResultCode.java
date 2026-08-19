package com.jing.security.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResultCode {

    SUCCESS(200, "success"),

    SYSTEM_ERROR(500, "系统异常"),

    BAD_REQUEST(400, "请求异常"),

    USERNAME_OR_PASSWORD_ERROR(10001, "用户名或密码错误"),

    UNAUTHORIZED(401, "未登录或认证信息无效"),

    FORBIDDEN(403, "无权限访问"),

    PARAM_ERROR(400,"参数错误");

    private final Integer code;

    private final String message;
}
