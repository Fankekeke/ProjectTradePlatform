package com.fate.archer.cos.dto.query;

import lombok.Data;

@Data
public class AccountVerify {

    private String account;

    private String receive;

    private Integer verifyType;

    private String verifyCode;

    private Integer messageType;

    public AccountVerify(String account, String receive, Integer verifyType, String verifyCode, Integer messageType) {
        this.account = account;
        this.receive = receive;
        this.verifyType = verifyType;
        this.verifyCode = verifyCode;
        this.messageType = messageType;
    }

    public AccountVerify() {}
}
