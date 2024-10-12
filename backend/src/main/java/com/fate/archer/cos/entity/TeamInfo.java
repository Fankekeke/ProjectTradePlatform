package com.fate.archer.cos.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 我们团队
 *
 * @author FanK
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class TeamInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 队伍成员
     */
    private String players;

    /**
     * 关联管理员
     */
    private Long adminUser;

    /**
     * 头像地址
     */
    private String avatar;

    /**
     * 账户余额
     */
    private BigDecimal balance;

    /**
     * 备注
     */
    private String remarks;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 工作时间
     */
    private String workTime;

    /**
     * 部署时间
     */
    private String deployTime;

    @TableField(exist = false)
    private Integer sex;


}
