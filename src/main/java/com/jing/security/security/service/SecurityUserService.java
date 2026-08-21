package com.jing.security.security.service;

import com.jing.security.security.model.LoginUser;

public interface SecurityUserService {

    LoginUser loadLoginUser(Long userId);
}
