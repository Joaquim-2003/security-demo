package com.jing.security.common.result;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.jing.security.common.enums.ResultCode;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@JsonPropertyOrder({"code", "message", "data"})
@Data
public class Result<T> implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Integer code;
    private String message;
    private T data;

    public static <T> Result<T> success() {
        Result<T> result = new Result<T>();
        result.code = ResultCode.SUCCESS.getCode();
        result.message = ResultCode.SUCCESS.getMessage();
        return result;
    }

    public static <T> Result<T> success(T data) {
        Result<T> result = new Result<T>();
        result.code = ResultCode.SUCCESS.getCode();
        result.message = ResultCode.SUCCESS.getMessage();
        result.data = data;
        return result;
    }

    public static <T> Result<T> error(ResultCode resultCode) {
        Result<T> result = new Result<T>();
        result.code = resultCode.getCode();
        result.message = resultCode.getMessage();
        return result;
    }

    public static <T> Result<T> error(ResultCode resultCode, String msg) {
        Result<T> result = new Result<T>();
        result.code = resultCode.getCode();
        result.message = msg;
        return result;
    }
}
