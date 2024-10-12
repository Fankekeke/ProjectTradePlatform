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
 * 附件数据
 *
 * @author FanK
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class AnnexInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Integer id;
    /**
     * 附件编号
     */
    private String code;
    /**
     * 附件名称
     */
    private String annexName;

    /**
     * 附件路径
     */
    private String annexLocal;

    /**
     * 类型 1.论文 2.PPT
     */
    private Integer annexType;

    /**
     * 作者
     */
    private Integer annexAnther;

    /**
     * 配套价格
     */
    private BigDecimal annexPrice;

    /**
     * 所属项目
     */
    private String proCode;

    /**
     * 是否启用
     */
    private Integer isEnable;

    /**
     * 上传时间
     */
    private String uploadDate;

    /**
     * 上传状态(1.正在审核 2.审核通过 3.审核不通过)
     */
    private Integer status;

    /**
     * 图册
     */
    private String pic;

    /**
     * 论文字数
     */
    private Integer wordCount;

    /**
     * 备注
     */
    private String remark;

    /**
     * 项目名称
     */
    @TableField(exist = false)
    private String proName;
    /**
     * 系统用户ID
     */
    @TableField(exist = false)
    private Integer userId;

}
