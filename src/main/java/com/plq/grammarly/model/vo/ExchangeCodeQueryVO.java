package com.plq.grammarly.model.vo;

import lombok.Data;

import java.util.Date;

/**
 * This is Description
 *
 * @author luquan.peng
 * @date 2021/06/17
 */
@Data
public class ExchangeCodeQueryVO {

    private String number;
    private String email;

    /**
     * 兑换状态
     */
    private Boolean exchangeStatus;
    /**
     * 是否到期 boolean
     */
    private Boolean expireStatus;

    private Date memberDeadlineStart;
    private Date memberDeadlineEnded;

    private Integer page;
    private Integer limit;
}
