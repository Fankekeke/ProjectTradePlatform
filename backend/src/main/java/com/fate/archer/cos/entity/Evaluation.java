package com.fate.archer.cos.entity;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;

import java.io.Serializable;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 用户评价
 *
 * @author FanK
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Evaluation implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Integer id;
    /**
     * 用户ID
     */
    @TableField("USER_CODE")
    private String userCode;

    /**
     * 评分
     */
    @TableField("SCORE")
    private BigDecimal score;

    /**
     * 评价
     */
    @TableField("CONTENT")
    private String content;

    /**
     * 所属订单
     */
    @TableField("ORDER_CODE")
    private String orderCode;

    /**
     * 证据
     */
    @TableField("PIC")
    private String pic;

    /**
     * 评价时间
     */
    @TableField("DATE")
    private String date;

    /**
     * 逻辑删除
     */
    @TableField("DELETE_FLAG")
    private Integer deleteFlag;

    @TableField(exist = false)
    private Integer paymentType;

    @TableField(exist = false)
    private String account;


}
