package com.jing.security.security.service.impl;

import com.jing.security.common.enums.ResultCode;
import com.jing.security.exception.BizException;
import com.jing.security.mapper.PermissionMapper;
import com.jing.security.mapper.SysUserMapper;
import com.jing.security.pojo.entity.SysUser;
import com.jing.security.security.model.LoginUser;
import com.jing.security.security.service.SecurityUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SecurityUserServiceImpl implements SecurityUserService {

    private final PermissionMapper permissionMapper;

    private final SysUserMapper sysUserMapper;

    @Override
    public LoginUser loadLoginUser(Long userId) {

        // 获取用户信息
        SysUser user = sysUserMapper.selectById(userId);

        if (user == null) {
            throw new BizException(ResultCode.UNAUTHORIZED);
        }

        // 获取用户权限
        List<String> permissionCodes = permissionMapper.selectPermissionCodeById(userId);

        // 将权限码转化为spring security可识别权限对象
        Collection<? extends GrantedAuthority> authorities =
                permissionCodes.stream().map(SimpleGrantedAuthority::new).toList();

        // 将用户信息 + 权限信息组装成 LoginUser
        return new LoginUser(user.getId(), user.getUsername(), authorities);
    }
}
