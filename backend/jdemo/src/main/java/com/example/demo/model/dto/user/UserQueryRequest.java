package com.example.demo.model.dto.user;

import com.example.demo.common.PageRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
public class UserQueryRequest extends PageRequest implements Serializable {
    private static final long serialVersionUID = 110L;

    private Long id;

    private String userName;

    private String userAccount;

    private String userAvatar;

    private Integer gender;

    private String userRole;

    private Date createTime;

    private Date updateTime;
}
