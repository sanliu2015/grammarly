package com.plq.grammarly.model.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.NotNull;

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
    @NotNull(message = "用户账号不能为空")
    private String account;

    /**
     * 用户密码
     */
    private String password;

    /**
     * 账户类型: 0-表示BUSINESS，1-EDU
     */
    @NotNull(message = "账户类型不能为空")
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
    @NotNull(message = "凭证信息不能为空")
    private String curlStr;

    private String errorMsg;

    private transient String typeName;
    private transient String curlIsSet;

}
