package com.jing.security.exception;

import com.jing.security.common.enums.ResultCode;
import lombok.Getter;

import java.io.Serial;

@Getter
public class BizException extends RuntimeException{

    @Serial
    private static final long serialVersionUID = 1L;

    private final ResultCode resultCode;

    public BizException(ResultCode resultCode){
        super(resultCode.getMessage());
        this.resultCode = resultCode;
    }

    public BizException(ResultCode resultCode, String message){
        super(message);
        this.resultCode = resultCode;
    }
}
