package com.fate.archer.cos.entity;

import java.math.BigDecimal;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 比率分配
 *
 * @author FanK
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class RateInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Integer id;
    /**
     * 角色标识
     */
    private String role;

    /**
     * 分配比率
     */
    private BigDecimal rate;

    /**
     * 1.定制 2.现成
     */
    private Integer flag;

    /**
     * 是否有邀请人
     */
    private Integer isInvite;


}
