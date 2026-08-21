package com.jing.security.mapper;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
@RequiredArgsConstructor
public class PermissionMapperTest {

    @Autowired
    private PermissionMapper permissionMapper;

    @Test
    void testSelectPermissionCodesByUserId() {
        List<String> permissions = permissionMapper.selectPermissionCodeById(1L);

        System.out.println(permissions);
    }
}
