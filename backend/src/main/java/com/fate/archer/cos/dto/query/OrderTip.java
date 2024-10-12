package com.fate.archer.cos.dto.query;

import lombok.Data;

@Data
public class OrderTip {

    private String data;

    private Integer verifyType;

    private Integer messageType;

    private String receive;

    public OrderTip(String data, Integer verifyType, Integer messageType, String receive) {
        this.data = data;
        this.verifyType = verifyType;
        this.messageType = messageType;
        this.receive = receive;
    }
}
