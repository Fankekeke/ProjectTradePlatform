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
public class ProductDetailVo implements Serializable {

    /**
     * 产品编号
     */
    private String code;

    /**
     * 产品名称
     */
    private String proName;

    /**
     * 产品介绍
     */
    private String proIntroduce;

    /**
     * 产品图册
     */
    private String proPic;

    /**
     * 产品价格
     */
    private BigDecimal proPrice;

    /**
     * 产品使用技术
     */
    private String proTechnology;

    /**
     * 项目类型
     */
    private Integer proType;

    /**
     * 项目分类
     */
    private String proSort;

    /**
     * 访问量
     */
    private Integer visits;

    /**
     * 订单成交量
     */
    private Integer turnover;

    /**
     * 发布时间
     */
    private String releaseTime;

    /**
     * 发布人
     */
    private String releaseUser;

    /**
     * 删除标识
     */
    private Integer delFlag;

    /**
     * 作者名称
     */
    private String authorName;

    /**
     * b站视频地址
     */
    private String videoAddress;
}
