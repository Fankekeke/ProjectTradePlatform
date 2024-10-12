package com.fate.archer.cos.entity;

import java.time.LocalDateTime;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 项目审核
 *
 * @author FanK
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ProjectApply implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Integer id;

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
     * 产品使用技术
     */
    private String proTechnology;

    /**
     * 视频地址
     */
    private String proVoide;

    /**
     * 项目文件
     */
    private String proDowbloadZip;

    /**
     * 项目类型(1.仅后台 2.前后台 3.小程序 4.安卓)
     */
    private Integer proType;

    /**
     * 项目分类
     */
    private String proSort;

    /**
     * 项目状态(1.正在审核 2.审核通过 3.审核不通过)
     */
    private Integer proStatus;

    /**
     * 发布时间
     */
    private String releaseTime;

    /**
     * 作者
     */
    private Integer author;


}
