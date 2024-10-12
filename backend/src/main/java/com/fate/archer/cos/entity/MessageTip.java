package com.fate.archer.cos.entity;

import java.time.LocalDateTime;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 消息通知
 *
 * @author FanK
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class MessageTip implements Serializable {
    /**
     * 发送方式
     */
    public static final Integer SEND_EMAIL = 1;

    public static final Integer SEND_SMS = 2;

    /**
     * 消息类型
     */
    public static final Integer MESSAGE_PAYMENT = 1;

    public static final Integer MESSAGE_ACCOUNT = 2;

    public static final Integer MESSAGE_ORDER = 3;

    public static final Integer MESSAGE_WITHDRAW = 4;

    public static final Integer MESSAGE_DAILY = 5;

    private static final long serialVersionUID = 1L;

    /**
     * 发送状态
     */
    public static final Integer MESSAGE_SUCCESS = 1;

    public static final Integer MESSAGE_FAIL = 2;

    /**
     * 发送时间
     */
    private LocalDateTime sendDate;

    /**
     * 发送账户
     */
    private Integer sendUser;

    /**
     * 发送方式
     */
    private Integer sendType;

    /**
     * 消息类型
     */
    private Integer messageType;

    /**
     * 状态
     */
    private Integer status;

    @TableField(exist = false)
    private String startDate;

    @TableField(exist = false)
    private String endDate;

    public MessageTip(Integer sendUser, Integer sendType, Integer messageType, Integer status) {
        this.sendUser = sendUser;
        this.sendType = sendType;
        this.messageType = messageType;
        this.status = status;
    }

    public MessageTip() {}
}
