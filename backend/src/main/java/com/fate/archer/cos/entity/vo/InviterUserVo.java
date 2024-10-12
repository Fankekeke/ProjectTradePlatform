package com.fate.archer.cos.entity.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author FanK
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class InviterUserVo implements Serializable {

    /**
     * 用户编号
     */
    private String code;

    /**
     * 账户名称
     */
    private String account;

    /**
     * 所属院校
     */
    private String schoolName;

    /**
     * 微信头像
     */
    private String headImgUrl;
}
