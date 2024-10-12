package com.fate.archer.cos.entity;

import java.time.LocalDateTime;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 部署流程
 *
 * @author FanK
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class DeployProcess implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Integer id;
    /**
     * 订单编号
     */
    private String orderCode;

    /**
     * 1.付款成功 2.部署完成 3.审核通过 4.未通过
     */
    private Integer status;

    /**
     * 操作证据
     */
    private String operatePic;

    /**
     * 操作时间
     */
    private String operateTime;

    /**
     * 当前选中（0.否 1.是）
     */
    private Integer currentStatus;

    public DeployProcess(String orderCode, Integer status, String operateTime, Integer currentStatus) {
        this.orderCode = orderCode;
        this.status = status;
        this.operateTime = operateTime;
        this.currentStatus = currentStatus;
    }

    public DeployProcess() {}


}
