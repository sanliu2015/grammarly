package com.plq.grammarly.model.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class QuestionExchangeCodeGenParamVO {

    @NotNull(message = "数量不能为空")
    private Integer count;

    private String deadline;
}
