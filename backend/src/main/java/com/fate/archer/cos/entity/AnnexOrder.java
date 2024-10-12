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
 * 附件订单
 *
 * @author FanK
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class AnnexOrder implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Integer id;
    /**
     * 附件订单编号
     */
    private String code;

    /**
     * 附件编号
     */
    private String annexCode;

    /**
     * 用户编号
     */
    private String userCode;

    /**
     * 价格
     */
    private BigDecimal price;

    /**
     * 支付方式(1.支付宝 2.微信 3.平台下单)
     */
    private Integer paymentType;

    /**
     * 下单时间
     */
    private String orderTime;

    /**
     * 附件名称
     */
    @TableField(exist = false)
    private String annexName;

    /**
     * 附件类型
     */
    @TableField(exist = false)
    private Integer annexType;

    /**
     * 用户名称
     */
    @TableField(exist = false)
    private String account;

}
