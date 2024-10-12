package com.fate.archer.cos.entity.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author FanK
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class AnnexDetailVo implements Serializable {
    
    /**
     * 附件编号
     */
    private String code;
    /**
     * 附件名称
     */
    private String annexName;

    /**
     * 类型 1.论文 2.PPT
     */
    private Integer annexType;

    /**
     * 作者
     */
    private String authorName;

    /**
     * 配套价格
     */
    private BigDecimal annexPrice;

    /**
     * 所属项目
     */
    private String proCode;

    /**
     * 上传时间
     */
    private String uploadDate;

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

}
