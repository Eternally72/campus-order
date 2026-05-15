package com.rabbit.user.service.impl;

import com.rabbit.common.dto.UserDTO;
import com.rabbit.common.entity.User;
import com.rabbit.common.exception.BusinessException;
import com.rabbit.common.result.ResultCodeEnum;
import com.rabbit.common.utils.IdUtil;
import com.rabbit.common.utils.PasswordUtil;
import com.rabbit.common.vo.UserVO;
import com.rabbit.user.mapper.UserMapper;
import com.rabbit.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

/**
 * 用户服务实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;

    @Override
    public UserVO getByUsername(String username) {
        User user = userMapper.selectByUsername(username);
        if (user == null) {
            return null;
        }
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        return userVO;
    }

    @Override
    public Long validatePassword(String username, String password) {
        User user = userMapper.selectByUsername(username);
        if (user == null) {
            throw new BusinessException(ResultCodeEnum.USER_NOT_EXIST);
        }
        // 验证密码
        if (!PasswordUtil.verify(password, user.getPassword())) {
            throw new BusinessException(ResultCodeEnum.USER_PASSWORD_ERROR);
        }
        // 检查用户状态
        if (user.getStatus() == 0) {
            throw new BusinessException(ResultCodeEnum.USER_DISABLED);
        }
        return user.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createUser(UserDTO userDTO) {
        // 检查用户名是否已存在
        User existUser = userMapper.selectByUsername(userDTO.getUsername());
        if (existUser != null) {
            throw new BusinessException(ResultCodeEnum.USER_ALREADY_EXIST);
        }

        User user = new User();
        BeanUtils.copyProperties(userDTO, user);
        // 生成ID
        user.setId(IdUtil.nextId());
        // 密码加密
        user.setPassword(PasswordUtil.encrypt(userDTO.getPassword()));
        user.setStatus(1);
        user.setDeleted(0);
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());

        userMapper.insert(user);

        log.info("创建用户成功: userId={}, username={}", user.getId(), user.getUsername());
        return user.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateUser(UserDTO userDTO) {
        User user = userMapper.selectById(userDTO.getId());
        if (user == null) {
            throw new BusinessException(ResultCodeEnum.USER_NOT_EXIST);
        }
        BeanUtils.copyProperties(userDTO, user);
        // 如果有更新密码
        if (userDTO.getPassword() != null && !userDTO.getPassword().isEmpty()) {
            user.setPassword(PasswordUtil.encrypt(userDTO.getPassword()));
        }
        userMapper.update(user);
        log.info("更新用户成功: userId={}", user.getId());
    }

    @Override
    public UserVO getUserById(Long id) {
        User user = userMapper.selectById(id);
        if (user == null) {
            throw new BusinessException(ResultCodeEnum.USER_NOT_EXIST);
        }
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        return userVO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteById(Long id) {
        userMapper.deleteById(id);
        log.info("删除用户成功: userId={}", id);
    }
}

