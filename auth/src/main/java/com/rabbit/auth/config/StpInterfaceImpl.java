package com.rabbit.auth.config;

import cn.dev33.satoken.stp.StpInterface;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Sa-Token权限认证接口实现
 * 自定义权限验证逻辑
 */
@Component
public class StpInterfaceImpl implements StpInterface {

    /**
     * 返回指定账号id所拥有的权限码集合
     * @param loginId   账号id
     * @param loginType 账号类型
     * @return 权限码集合
     */
    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        if (isAdmin(loginId)) {
            return List.of("*");
        }
        return List.of(
                "goods:create",
                "goods:update:self",
                "order:create",
                "order:update:self",
                "favorite:manage",
                "notify:read"
        );
    }

    /**
     * 返回指定账号id所拥有的角色标识集合
     *
     * @param loginId   账号id
     * @param loginType 账号类型
     * @return 角色标识集合
     */
    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        return isAdmin(loginId) ? List.of("admin", "user") : List.of("user");
    }

    private boolean isAdmin(Object loginId) {
        return loginId != null && "1".equals(loginId.toString());
    }
}

