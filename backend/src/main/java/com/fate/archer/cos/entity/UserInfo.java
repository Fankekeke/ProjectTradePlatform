package com.fate.archer.cos.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 用户管理
 *
 * @author FanK
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class UserInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 用户编号
     */
    private String code;

    /**
     * 账户名称
     */
    private String account;

    /**
     * 邮箱地址
     */
    private String email;

    /**
     * 手机号码
     */
    private String phone;

    /**
     * 密码
     */
    private String password;

    /**
     * 所属院校
     */
    private String school;

    /**
     * 所属院校（值）
     */
    private String schoolName;

    /**
     * 账户状态 0锁定 1有效
     */
    private Integer status;

    /**
     * 性别 0男 1女 2保密
     */
    private Integer sex;

    /**
     * 是否为邀请人 0否 1是
     */
    private Integer invite;

    /**
     * 账户余额
     */
    private BigDecimal balance;

    /**
     * 账户创建时间
     */
    private LocalDateTime createDate;

    /**
     * 微信OPEN
     */
    private String openId;

    /**
     * 邀请人编号
     */
    private String inviteUser;

    /**
     * 微信头像
     */
    private String headImgUrl;

    /**
     * 最后上线时间
     */
    private String lastLoginDate;

}
