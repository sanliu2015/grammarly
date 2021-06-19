package com.plq.grammarly.exception;

import org.springframework.http.HttpStatus;

/**
 * 自定义异常
 *
 * @author g-signature
 */
public class CustomException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    private final String message;
    private Integer code;

    public CustomException(String message) {
        this.message = message;
        this.code = HttpStatus.INTERNAL_SERVER_ERROR.value();
    }

    public CustomException(String message, Integer code) {
        this.message = message;
        this.code = code;
    }

    public CustomException(String message, Throwable e) {
        super(message, e);
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public Integer getCode() {
        return code;
    }
}
