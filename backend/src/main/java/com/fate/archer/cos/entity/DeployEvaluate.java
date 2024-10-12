package com.fate.archer.cos.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 部署审核
 *
 * @author FanK
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class DeployEvaluate implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Integer id;
    /**
     * 订单编号
     */
    private String orderCode;

    /**
     * 图片证据
     */
    private String evidence;

    /**
     * 备注
     */
    private String remark;

    /**
     * 审核状态
     */
    @TableField(exist = false)
    private Integer status;
}
