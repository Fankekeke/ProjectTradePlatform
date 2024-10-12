package com.fate.archer.cos.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 提现申请
 *
 * @author FanK
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class TakeReview implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Integer id;
    /**
     * 所属用户
     */
    private String userCode;

    /**
     * 审理状态
     */
    private Integer review;

    /**
     * 支付方式（1.支付宝 2.微信 3.其他）
     */
    private Integer tradeType;

    /**
     * 备注
     */
    private String remarks;

    /**
     * 提现金额
     */
    private BigDecimal balance;

    /**
     * 用户收款码
     */
    private String receiving;

    /**
     * 交易结果图片
     */
    private String tradeResult;

    /**
     * 申请日期
     */
    private String date;

    /**
     * 交易完成时间
     */
    private String finishDate;

    @TableField(exist = false)
    private String account;

}
