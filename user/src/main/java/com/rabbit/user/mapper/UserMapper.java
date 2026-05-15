package com.rabbit.user.mapper;

import com.rabbit.common.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户Mapper
 */
@Mapper
public interface UserMapper {

    /**
     * 根据ID查询用户
     */
    User selectById(@Param("id") Long id);

    /**
     * 根据用户名查询用户
     */
    User selectByUsername(@Param("username") String username);

    /**
     * 插入用户
     */
    int insert(User user);

    /**
     * 更新用户
     */
    int update(User user);

    /**
     * 根据ID删除用户（逻辑删除）
     */
    int deleteById(@Param("id") Long id);

    /**
     * 查询用户列表
     */
    List<User> selectList();

    /**
     * 根据手机号查询用户
     */
    User selectByPhone(@Param("phone") String phone);
}

