package com.fate.archer.cos.entity;

import java.time.LocalDateTime;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 公告信息
 *
 * @author FanK
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class NoticeInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 公告编号
     */
    private String code;

    /**
     * 公告标题
     */
    private String messTitle;

    /**
     * 发帖人
     */
    private String messUser;

    /**
     * 内容
     */
    private String content;

    /**
     * 发帖时间
     */
    private String sendDate;

    /**
     * 访问量
     */
    private Integer accessNum;

    /**
     * 公告类型
     */
    private Integer messType;


}
