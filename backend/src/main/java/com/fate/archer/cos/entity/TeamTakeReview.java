package com.fate.archer.cos.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 开发者提现申请
 *
 * @author FanK
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class TeamTakeReview implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 开发者ID
     */
    private Integer teamId;

    /**
     * 审理状态（1.审核中 2.正在交易 3.交易完成）
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
     * 提现金额（提现全部）
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

    /**
     * 开发者名称
     */
    @TableField(exist = false)
    private String players;

    /**
     * 开发者头像
     */
    @TableField(exist = false)
    private String avatar;

}
