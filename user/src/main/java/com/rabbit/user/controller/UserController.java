package com.rabbit.user.controller;

import com.rabbit.common.dto.UserDTO;
import com.rabbit.common.result.Result;
import com.rabbit.common.vo.UserVO;
import com.rabbit.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 用户控制器
 */
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * 根据ID获取用户信息
     */
    @GetMapping("/{id}")
    public Result<UserVO> getById(@PathVariable Long id) {
        UserVO userVO = userService.getUserById(id);
        return Result.success(userVO);
    }

    /**
     * 根据用户名获取用户信息
     */
    @GetMapping("/getByUsername")
    public Result<UserVO> getByUsername(@RequestParam String username) {
        UserVO userVO = userService.getByUsername(username);
        return Result.success(userVO);
    }

    /**
     * 验证用户密码
     */
    @GetMapping("/validatePassword")
    public Result<Long> validatePassword(@RequestParam String username,
                                          @RequestParam String password) {
        Long userId = userService.validatePassword(username, password);
        return Result.success(userId);
    }

    /**
     * 创建用户
     */
    @PostMapping
    public Result<Long> createUser(@Valid @RequestBody UserDTO userDTO) {
        Long userId = userService.createUser(userDTO);
        return Result.success(userId);
    }

    /**
     * 更新用户信息
     */
    @PutMapping
    public Result<Void> updateUser(@Valid @RequestBody UserDTO userDTO) {
        userService.updateUser(userDTO);
        return Result.success();
    }

    /**
     * 删除用户
     */
    @DeleteMapping("/{id}")
    public Result<Void> deleteUser(@PathVariable Long id) {
        userService.deleteById(id);
        return Result.success();
    }
}

