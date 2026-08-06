package com.jing.security.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResultCode {

    SUCCESS(1, "success"),

    ERROR(0, "error");

    private final Integer code;

    private final String message;
}
