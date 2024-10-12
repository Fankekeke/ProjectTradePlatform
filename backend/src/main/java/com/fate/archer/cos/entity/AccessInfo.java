package com.fate.archer.cos.entity;

import java.time.LocalDateTime;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 访问信息
 *
 * @author FanK
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class AccessInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 访问IP
     */
    private String accessIp;

    /**
     * 访问地区
     */
    private String accessArea;

    /**
     * 访问时间
     */
    private String accessTime;
    /**
     * 开始时间
     */
    @TableField(exist = false)
    private String startDate;
    /**
     * 结束时间
     */
    @TableField(exist = false)
    private String endDate;

    public AccessInfo(String accessIp, String accessArea, String accessTime) {
        this.accessIp = accessIp;
        this.accessArea = accessArea;
        this.accessTime = accessTime;
    }


}
