package com.rabbit.common.result;

import lombok.Getter;

/**
 * 响应码枚举
 */
@Getter
public enum ResultCodeEnum {

    // 成功
    SUCCESS(200, "操作成功"),

    // 客户端错误 4xx
    FAIL(400, "操作失败"),
    PARAM_ERROR(400, "参数错误"),
    UNAUTHORIZED(401, "未登录或登录已过期"),
    FORBIDDEN(403, "没有权限访问"),
    NOT_FOUND(404, "资源不存在"),
    METHOD_NOT_ALLOWED(405, "请求方法不允许"),

    // 服务端错误 5xx
    SYSTEM_ERROR(500, "系统错误"),
    SERVICE_UNAVAILABLE(503, "服务不可用"),

    // 业务错误 1xxx
    USER_NOT_EXIST(1001, "用户不存在"),
    USER_PASSWORD_ERROR(1002, "密码错误"),
    USER_DISABLED(1003, "用户已被禁用"),
    USER_ALREADY_EXIST(1004, "用户已存在"),

    // 商品相关 2xxx
    GOODS_NOT_EXIST(2001, "商品不存在"),
    GOODS_STOCK_NOT_ENOUGH(2002, "商品库存不足"),
    GOODS_OFF_SHELF(2003, "商品已下架"),

    // 订单相关 3xxx
    ORDER_NOT_EXIST(3001, "订单不存在"),
    ORDER_STATUS_ERROR(3002, "订单状态异常"),
    ORDER_CREATE_FAIL(3003, "订单创建失败"),
    ORDER_PAY_FAIL(3004, "订单支付失败"),
    ORDER_CANCEL_FAIL(3005, "订单取消失败"),

    // 认证相关 4xxx
    TOKEN_INVALID(4001, "Token无效"),
    TOKEN_EXPIRED(4002, "Token已过期"),
    LOGIN_FAIL(4003, "登录失败");

    /**
     * 响应码
     */
    private final Integer code;

    /**
     * 响应消息
     */
    private final String message;

    ResultCodeEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}

