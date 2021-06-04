package com.plq.grammarly.model.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

/**
 * This is Description
 *
 * @author luquan.peng
 * @date 2021/06/03
 */
@Data
@Document
public class ExchangeCode {

    @Id
    private String id;

    /**
     * 兑换码
     */
    @Indexed(unique = true)
    private String number;
    /**
     * 兑换码兑换后激活的grammar账号有效天数
     */
    private Integer validDays;

    /**
     * 兑换截止时间，产生1个兑换码，必须在最后期限之前兑换
     */
    private Date exchangeDeadline;

    /**
     * 生成日期
     */
    @CreatedDate
    private Date createTime;
    /**
     * 是否兑换 boolean
     */
    private Boolean exchangeStatus;
    /**
     * 用户邮箱
     */
    private String email;

    /**
     * 兑换日期
     */
    private Date exchangeTime;

    /**
     * 会员到期日期=兑换日期exchange+有效天数validDays（只到天不到时分秒）
     */
    private Date memberDeadline;

    /**
     * 邀请方账号
     */
    private String inviterAccount;

    /**
     * 是否到期 boolean
     */
    private Boolean expireStatus;

    /**
     * grammarly上面 是否移除该账号
     */
    private Boolean removeStatus;

    /**
     * 移除时间
     */
    private Date removeTime;

    private String errorMsg;

    @LastModifiedDate
    private Date updateTime;



}
