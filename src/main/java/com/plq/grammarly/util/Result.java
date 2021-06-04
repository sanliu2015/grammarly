package com.plq.grammarly.util;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * 返回响应
 *
 * @author luquan.peng
 * @date 2021/06/03
 */
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
public class Result<T> implements Serializable {

    private Integer code;

    private String msg;

    private T data;

    public Result(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Result(Integer code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static <T> Result<T> success() {
        return new Result<>(ResultEnum.SUCCESS.getCode(), ResultEnum.SUCCESS.getMsg());
    }

    public static <T> Result<T> success(T data) {
        return new Result<>(ResultEnum.SUCCESS.getCode(), ResultEnum.SUCCESS.getMsg(), data);
    }

    public static <T> Result<T> failure() {
        return new Result<>(ResultEnum.SYSTEM_INTERNAL_ERROR.getCode(), ResultEnum.SYSTEM_INTERNAL_ERROR.getMsg());
    }

    public static <T> Result<T> failure(String msg) {
        return new Result<>(ResultEnum.SYSTEM_INTERNAL_ERROR.getCode(), msg);
    }

}
