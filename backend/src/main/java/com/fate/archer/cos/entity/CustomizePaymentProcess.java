package com.fate.archer.cos.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 定制项目支付流程
 *
 * @author FanK
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class CustomizePaymentProcess implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 定制订单编号
     */
    private String customCode;

    /**
     * 定金
     */
    private BigDecimal frontMoney;

    /**
     * 定金状态（0.未支付 1.已支付）
     */
    private Integer frontStatus;

    /**
     * 定金支付时间
     */
    private String frontDate;

    /**
     * 定金支付方式
     */
    private Integer frontType;

    /**
     * 尾款
     */
    private BigDecimal finalMoney;

    /**
     * 尾款状态（0.未支付 1.已支付）
     */
    private Integer finalStatus;

    /**
     * 尾款支付时间
     */
    private String finalDate;

    /**
     * 尾款支付方式
     */
    private Integer finalType;


}
