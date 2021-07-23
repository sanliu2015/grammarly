package com.plq.grammarly.model.entity;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * 邮箱设置
 *
 * @author luquan.peng
 * @date 2021/07/23
 */
@Document
@Builder
@Data
public class EmailConfig implements Serializable {

    @Id
    private String id;

    @NotNull(message = "邮箱不能为空")
    private String email;

    @NotNull(message = "密码不能为空")
    private String password;

    @NotNull(message = "host不能为空")
    private String host;

    private boolean sslEnable;

    @CreatedDate
    private Date createTime;

    @LastModifiedDate
    private Date updateTime;

}
