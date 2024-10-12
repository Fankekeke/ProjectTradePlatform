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
 * 订单信息
 *
 * @author FanK
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class OrderInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 订单编号
     */
    private String code;

    /**
     * 项目编号
     */
    private String proCode;

    /**
     * 订单价格
     */
    private BigDecimal orderPrice;

    /**
     * 下单时间
     */
    private String orderTime;

    /**
     * 支付方式
     */
    private Integer paymentType;

    /**
     * 兑换码
     */
    private String exchangeCode;

    /**
     * 兑换地址
     */
    private String exchangeUrl;

    /**
     * 兑换码有效时间
     */
    private String effectiveTime;

    /**
     * 收货人
     */
    private String userCode;

    /**
     * 订单状态
     */
    private Integer orderStatus;

    /**
     * 是否需要部署
     */
    private Integer needDeploy;

    /**
     * 邀请人
     */
    private String agent;

    /**
     * 项目名称
     */
    @TableField(exist = false)
    private String proName;

    /**
     * 是否过期
     */
    @TableField(exist = false)
    private Integer isExpired;

    /**
     * 购买人
     */
    @TableField(exist = false)
    private String account;

    /**
     * 当前部署状态
     */
    @TableField(exist = false)
    private Integer deployStatus;


}
