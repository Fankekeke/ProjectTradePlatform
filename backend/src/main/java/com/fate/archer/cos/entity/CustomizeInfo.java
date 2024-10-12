package com.fate.archer.cos.entity;

import java.math.BigDecimal;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 项目定制
 *
 * @author FanK
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class CustomizeInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Integer id;
    /**
     * 定制订单编号
     */
    private String code;

    /**
     * 系统名称
     */
    private String proName;

    /**
     * 功能模块
     */
    private String module;

    /**
     * 技术要求
     */
    private String skills;

    /**
     * 办理用户
     */
    private String userCode;

    /**
     * 定金
     */
    private BigDecimal deposit;

    /**
     * 全款
     */
    private BigDecimal amount;

    /**
     * 状态 0.待审核 1.沟通中 2.已接单 3.已完成 4.部署完成 5.审核完成 6.审核不通过
     */
    private Integer status;

    /**
     * 分配技术人员
     */
    private String technician;

    /**
     * 备注
     */
    private String remarks;

    /**
     * 用户联系方式
     */
    private String contact;

    /**
     * 项目周期(天)
     */
    private Integer cycle;

    /**
     * 项目开始时间
     */
    private String startDate;

    /**
     * 定制论文编号
     */
    private String thesisCode;

    /**
     * 创建时间
     */
    private String createDate;
}
