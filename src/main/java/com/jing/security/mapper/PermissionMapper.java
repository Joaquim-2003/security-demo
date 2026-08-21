package com.jing.security.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PermissionMapper {

    List<String> selectPermissionCodeById(Long userId);
}
