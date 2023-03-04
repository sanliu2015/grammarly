package com.plq.grammarly.model.vo;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
public class QuestionExchangeParamVO {

    @NotNull(message = "兑换码不能为空")
    @NotBlank(message = "兑换码不能为空格")
    private String code;

    @NotNull(message = "邮箱不能为空")
    @Email
    private String receiveEmail;

    @NotNull(message = "问题url不能为空")
    private String questionUrl;

}
