package com.rabbit.common.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 验证码响应VO。
 */
@Data
public class CaptchaVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String captchaKey;

    private String captcha;
}
