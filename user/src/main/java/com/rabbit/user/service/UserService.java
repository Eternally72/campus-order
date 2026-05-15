package com.rabbit.user.service;

import com.rabbit.common.dto.UserDTO;
import com.rabbit.common.vo.UserVO;

/**
 * 用户服务接口
 */
public interface UserService {

    /**
     * 根据用户名查询用户
     */
    UserVO getByUsername(String username);

    /**
     * 验证用户密码
     */
    Long validatePassword(String username, String password);

    /**
     * 创建用户
     */
    Long createUser(UserDTO userDTO);

    /**
     * 更新用户信息
     */
    void updateUser(UserDTO userDTO);

    /**
     * 根据ID获取用户信息
     */
    UserVO getUserById(Long id);

    /**
     * 根据ID删除用户
     */
    void deleteById(Long id);
}

