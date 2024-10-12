package com.fate.archer.cos.dto.query;

import lombok.Data;

@Data
public class TakeMoney {

    private String user;

    private String money;

    private Integer status;

    private Integer sendType;

    private String receive;

    private Integer messageType;
}
