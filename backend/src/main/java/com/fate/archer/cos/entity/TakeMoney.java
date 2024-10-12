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
 * 用户提现
 *
 * @author FanK
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class TakeMoney implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Integer id;
    /**
     * 所属用户
     */
    private Integer userId;

    /**
     * 提现金额
     */
    private BigDecimal takeMoney;

    /**
     * 剩余金额
     */
    private BigDecimal laveMoney;

    /**
     * 提现日期
     */
    private LocalDateTime date;


}
