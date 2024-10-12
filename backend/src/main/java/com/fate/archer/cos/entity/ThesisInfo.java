package com.fate.archer.cos.entity;

import java.math.BigDecimal;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 定制论文信息
 *
 * @author FanK
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ThesisInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 定制论文编号
     */
    private String code;

    /**
     * 论文名称
     */
    private String thesisName;

    /**
     * 字数要求
     */
    private Integer wordCount;

    /**
     * 查重率
     */
    private Integer duplicate;

    /**
     * 论文价格
     */
    private BigDecimal price;

    /**
     * 项目周期
     */
    private Integer cycle;

    /**
     * 论文分配人
     */
    private Long technician;

}
