package com.plq.grammarly.model.entity;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.Date;

/**
 * This is Description
 *
 * @author luquan.peng
 * @date 2021/06/16
 */
@Document
@Builder
@Data
public class User implements Serializable {

    @Id
    private String id;
    private String username;
    private String password;
    private Boolean active;
    private String roles;

    // additional fields to demonstrate we can store/retreive custom info too
    private String company;
    private String department;

    @CreatedDate
    private Date createTime;

    @LastModifiedDate
    private Date updateTime;
}
