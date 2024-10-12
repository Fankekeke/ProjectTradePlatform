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
 * 定制申请
 *
 * @author FanK
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class CustomApplication implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 编号
     */
    private String code;

    /**
     * 用户编号
     */
    private String userCode;

    /**
     * 系统名称
     */
    private String proName;

    /**
     * 技术要求
     */
    private String skills;

    /**
     * 功能模块
     */
    private String module;

    /**
     * 用户联系方式
     */
    private String contact;

    /**
     * 定制编号
     */
    private String customizeCode;

    /**
     * 创建时间
     */
    private String createDate;

    /**
     * 全款
     */
    @TableField(exist = false)
    private BigDecimal amount;

    /**
     * 项目开始时间
     */
    @TableField(exist = false)
    private String startDate;

    /**
     * 备注
     */
    @TableField(exist = false)
    private String remarks;

    /**
     * 是否需要论文
     */
    @TableField(exist = false)
    private Integer thesis;

    /**
     * 论文名称
     */
    @TableField(exist = false)
    private String thesisName;

    /**
     * 字数要求
     */
    @TableField(exist = false)
    private Integer wordCount;

    /**
     * 查重率
     */
    @TableField(exist = false)
    private Integer duplicate;

    /**
     * 论文价格
     */
    @TableField(exist = false)
    private BigDecimal price;

    /**
     * 项目周期
     */
    @TableField(exist = false)
    private Integer cycle;

    /**
     * 论文项目周期
     */
    @TableField(exist = false)
    private Integer thesisCycle;

    /**
     * 论文分配人
     */
    @TableField(exist = false)
    private Long technician;
}
