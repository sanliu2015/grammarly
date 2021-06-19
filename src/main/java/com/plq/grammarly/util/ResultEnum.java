package com.plq.grammarly.util;

import org.springframework.http.HttpStatus;

/**
 * This is Description
 *
 * @author luquan.peng
 * @date 2021/06/03
 */
public enum ResultEnum {

    // 成功
    SUCCESS(HttpStatus.OK.value(), "success"),
    // 参数错误
    PARAMETER_ILLEGAL(HttpStatus.BAD_REQUEST.value(), "parameter illegal"),
    // 404错误
    NOT_FUND(HttpStatus.NOT_FOUND.value(), "404 not found"),
    // 内部500
    SYSTEM_INTERNAL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR.value(), "system internal error");

    private final Integer code;
    private final String msg;

    ResultEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
