package com.fzq.tapibackend.model.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * User table
 * @TableName User
 */
@TableName(value ="User")
@Data
public class User implements Serializable {
    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * User Name
     */
    @TableField(value = "user_name")
    private String userName;

    /**
     * User Account
     */
    @TableField(value = "user_account")
    private String userAccount;

    /**
     * User Avatar
     */
    @TableField(value = "user_avatar")
    private String userAvatar;

    /**
     * email
     */
    @TableField(value = "email")
    private String email;

    /**
     * user Role：user / admin
     */
    @TableField(value = "user_role")
    private String userRole;

    /**
     * user password
     */
    @TableField(value = "user_password")
    private String userPassword;

    /**
     * user balance, 30 by default on signing up
     */
    @TableField(value = "balance")
    private Long balance;

    /**
     * account status（0- normal 1- blocked）
     */
    @TableField(value = "status")
    private Integer status;

    /**
     * create time
     */
    @TableField(value = "create_time")
    private Date createTime;

    /**
     * update time
     */
    @TableField(value = "update_time")
    private Date updateTime;

    /**
     * is delete? 1- deleted
     */
    @TableLogic
    @TableField(value = "is_delete")
    private Integer isDelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}