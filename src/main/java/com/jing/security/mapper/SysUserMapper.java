package com.jing.security.mapper;

import com.jing.security.pojo.entity.SysUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface SysUserMapper {

    @Select("select id, username, password from sys_user where id = #{userId}")
    SysUser selectById(Long userId);
}
