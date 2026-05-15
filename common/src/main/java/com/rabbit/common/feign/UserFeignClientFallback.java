package com.rabbit.common.feign;

import com.rabbit.common.result.Result;
import com.rabbit.common.dto.UserDTO;
import com.rabbit.common.vo.UserVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * 用户服务Feign降级工厂
 */
@Slf4j
@Component
public class UserFeignClientFallback implements FallbackFactory<UserFeignClient> {

    @Override
    public UserFeignClient create(Throwable cause) {
        log.error("用户服务调用失败", cause);
        return new UserFeignClient() {
            @Override
            public Result<UserVO> getUserById(Long id) {
                return Result.fail("用户服务不可用");
            }

            @Override
            public Result<UserVO> getByUsername(String username) {
                return Result.fail("用户服务不可用");
            }

            @Override
            public Result<Long> validatePassword(String username, String password) {
                return Result.fail("用户服务不可用");
            }

            @Override
            public Result<Long> createUser(UserDTO userDTO) {
                return Result.fail("用户服务不可用");
            }
        };
    }
}

