package com.jing.security.controller;

import com.jing.security.common.enums.ResultCode;
import com.jing.security.common.result.Result;
import com.jing.security.dto.UserDto;
import com.jing.security.exception.BizException;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/test")
public class TestController {

    @GetMapping
    public Result<Void> test() {
        throw new BizException(ResultCode.SYSTEM_ERROR);
    }

    @PostMapping("/valid")
    public Result<Void> testValid(@Valid @RequestBody UserDto userDto) {
        return Result.success();
    }
}
