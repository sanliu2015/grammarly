package com.plq.grammarly.model.vo;

import lombok.Data;

@Data
public class QuestionExchangeCodeQueryVO {

    private String code;
    private String receiveEmail;
    // 0未兑换，1已兑换，2兑换出错，3兑换过期
    private String status;
    private Integer page;
    private Integer limit;

}
