package com.fate.archer.cos.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 消息模板
 *
 * @author FanK
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class TipInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 消息内容
     */
    private String content;

    /**
     * 标识
     */
    private String mark;

    /**
     * 所属角色 1.管理员 2.开发者 3.用户
     */
    private Integer belongRole;

    /**
     * 参数大小
     */
    private Integer parameter;

    /**
     * 删除标识
     */
    private Integer delFlag;


}
