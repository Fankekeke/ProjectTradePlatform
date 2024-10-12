package com.fate.archer.cos.entity;

import java.time.LocalDateTime;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 定制项目加时
 *
 * @author FanK
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class CustomizeOvertime implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Integer id;
    /**
     * 定制订单编号
     */
    private String customizeCode;

    /**
     * 加时天数
     */
    private Integer overtimeDay;

    /**
     * 提出人
     */
    private Integer proposer;

    /**
     * 审核状态 1.正在审核 2.审核通过 3.审核不通过
     */
    private Integer status;

    /**
     * 创建时间
     */
    private String createDate;


}
