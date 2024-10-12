package com.fate.archer.cos.entity;

import java.time.LocalDateTime;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 定制项目台账
 *
 * @author FanK
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class CustomizeLedger implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Integer id;
    /**
     * 定制项目编号
     */
    private String customizeCode;

    /**
     * 项目路径
     */
    private String local;

    /**
     * 项目图册
     */
    private String pic;

    /**
     * 备注
     */
    private String remarks;

    /**
     * 项目完成时间
     */
    private String completeDate;

    /**
     * 项目上传人
     */
    private Integer proposer;

    /**
     * 部署证据
     */
    private String deployPic;

    /**
     * 部署完成时间
     */
    private String deployDate;

    /**
     * 部署人
     */
    private Integer deployUser;


}
