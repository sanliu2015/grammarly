package com.plq.grammarly.model.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.Date;

/**
 * This is Description
 *
 * @author luquan.peng
 * @date 2021/06/04
 */
@Data
@Document
public class GrammarlyAccount implements Serializable {

    @Id
    private String id;

    /**
     * 用户账号
     */
    @Indexed(unique = true)
    private String account;

    /**
     * 用户密码
     */
    private String password;

    /**
     * 账户类型: 0-表示月卡以下，1-月卡及以上
     */
    private String accountType;
    /**
     * 生成日期
     */
    @CreatedDate
    private Date createTime;
    /**
     * 修改日期
     */
    @LastModifiedDate
    private Date updateTime;

    /**
     * 身份凭证信息curl字符串
     */
    private String curlStr;

    private String errorMsg;

}
