package com.fate.archer.cos.entity;

import java.math.BigDecimal;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 项目收益分配记录
 *
 * @author FanK
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class WealthAssign implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 订单编号
     */
    private String orderCode;

    /**
     * 开发者编号
     */
    @TableField(value="developer_a")
    private Integer developera;

    /**
     * 开发者编号
     */
    @TableField(value="developer_b")
    private Integer developerb;

    /**
     * 开发者编号
     */
    @TableField(value="developer_c")
    private Integer developerc;

    /**
     * 开发者收益
     */
    private BigDecimal developerBalance;

    /**
     * 平台收益
     */
    private BigDecimal platformBalance;

    /**
     * 邀请人编号
     */
    private String inviteCode;

    /**
     * 邀请人收益
     */
    private BigDecimal inviteBalance;

    /**
     * 分款金额
     */
    private BigDecimal wealth;

    /**
     * 分配时间
     */
    private String createDate;


}
