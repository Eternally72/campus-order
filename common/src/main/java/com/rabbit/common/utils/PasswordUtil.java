package com.rabbit.common.utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;

/**
 * 密码工具类
 */
public class PasswordUtil {

    /**
     * 盐值（可配置化）
     */
    private static final String TEXT = "campus-order-salt";

    /**
     * 加密密码
     */
    public static String encrypt(String password) {
        return sha256(password);
    }

    /**
     * 验证密码
     */
    public static boolean verify(String rawPassword, String encodedPassword) {
        return encrypt(rawPassword).equals(encodedPassword);
    }

    /**
     * SHA256加密
     */
    public static String sha256(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] bytes = digest.digest((password + TEXT).getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(bytes);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 algorithm is not available", e);
        }
    }
}

