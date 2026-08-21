package com.jing.security.security.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@Data
@AllArgsConstructor
public class LoginUser {

    private Long id;

    private String username;

    // 集合类型声明，允许放 GrantedAuthority 的各种实现类型
    private Collection<? extends GrantedAuthority> authorities;
}
