package com.fate.archer.cos.dto.query;

import lombok.Data;

@Data
public class PayMessage {

    private String userName;

    private String user;

    private String money;

    private String thing;

    private Integer sendType;

    private String receive;

    private Integer messageType;
}
