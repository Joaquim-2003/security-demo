package com.jing.security.pojo.entity;

import lombok.Data;

@Data
public class SysUser {

    private final Long id;

    private final String username;

    private final String password;
}
