package com.jing.security;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class EncodePasswordTest {

    @Test
    void encodePassword() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        String password = encoder.encode("123456");

        System.out.println(password);
    }
}
