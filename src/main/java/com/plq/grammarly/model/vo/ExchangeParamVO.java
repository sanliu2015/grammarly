package com.plq.grammarly.model.vo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * This is Description
 *
 * @author luquan.peng
 * @date 2021/06/04
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
public class ExchangeParamVO {

    @NotNull(message = "兑换码不能为空")
    @NotBlank(message = "兑换码不能为空格")
    private String number;

    @NotNull(message = "邮箱不能为空")
    @Email
    private String email;
}
