package com.jing.security.service.impl;

import com.jing.security.common.enums.ResultCode;
import com.jing.security.exception.BizException;
import com.jing.security.mapper.SysUserMapper;
import com.jing.security.pojo.dto.LoginDTO;
import com.jing.security.pojo.entity.SysUser;
import com.jing.security.pojo.vo.LoginVO;
import com.jing.security.service.AuthService;
import com.jing.security.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final SysUserMapper sysUserMapper;

    private final PasswordEncoder passwordEncoder;

    private final JwtUtil jwtUtil;

    @Override
    public LoginVO login(LoginDTO loginDTO) {

        SysUser sysUser = sysUserMapper.selectByUsername(loginDTO.getUsername());

        // 判断username是否存在
        if (sysUser == null) {
            throw new BizException(ResultCode.USERNAME_OR_PASSWORD_ERROR);
        }

        // 判断password是否正确
        if (!passwordEncoder.matches(loginDTO.getPassword(), sysUser.getPassword())) {
            throw new BizException(ResultCode.USERNAME_OR_PASSWORD_ERROR);
        }

        // 生成token
        String token  = jwtUtil.generateToken(sysUser.getId(), sysUser.getUsername());

        return new LoginVO(token);
    }
}
