package com.fate.archer.cos.entity.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author FanK
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class IncomeDetailVo implements Serializable {

    /**
     * 订单编号
     */
    private String orderCode;

    /**
     * 收益类型 1.成品收益 2.定制收益 3.附件收益
     */
    private String incomeType;

    /**
     * 订单名称
     */
    private String orderName;

    /**
     * 总金额
     */
    private BigDecimal wealthAmount;

    /**
     * 分款金额
     */
    private BigDecimal partWealth;

    /**
     * 分款时间
     */
    private String createDate;

    /**
     * 用户名称
     */
    private String userName;

    /**
     * 用户头像
     */
    private String avatar;


}
