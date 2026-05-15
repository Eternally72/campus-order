package com.rabbit.common.feign;

import com.rabbit.common.result.Result;
import com.rabbit.common.dto.UserDTO;
import com.rabbit.common.vo.UserVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 用户服务Feign接口（公共）
 */
@FeignClient(name = "user-service", contextId = "commonUserFeignClient", fallbackFactory = UserFeignClientFallback.class)
public interface UserFeignClient {

    /**
     * 根据ID获取用户信息
     */
    @GetMapping("/user/{id}")
    Result<UserVO> getUserById(@PathVariable Long id);

    /**
     * 根据用户名获取用户信息
     */
    @GetMapping("/user/getByUsername")
    Result<UserVO> getByUsername(@RequestParam("username") String username);

    /**
     * 验证用户密码
     */
    @GetMapping("/user/validatePassword")
    Result<Long> validatePassword(@RequestParam("username") String username,
                                   @RequestParam("password") String password);

    /**
     * 创建用户
     */
    @PostMapping("/user")
    Result<Long> createUser(@RequestBody UserDTO userDTO);
}

