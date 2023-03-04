package com.plq.grammarly.model.entity;


import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;


@Document("question_exchange_code")
@Builder
@Data
public class QuestionExchangeCode implements Serializable {

    @Id
    private String id;
    private String code;
    private String status;
    private String deadline;
    private String createTime;
    private String questionUrl;
    private String receiveEmail;
    private String errmsg;
    private String updateTime;
    private String account;

    @Tolerate
    public QuestionExchangeCode() {}
}
