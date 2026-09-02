package com.jing.security.service;

import com.jing.security.pojo.dto.LoginDTO;
import com.jing.security.pojo.vo.LoginVO;

public interface AuthService {

    LoginVO login(LoginDTO loginDTO);
}
