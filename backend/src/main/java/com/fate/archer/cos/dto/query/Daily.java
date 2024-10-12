package com.fate.archer.cos.dto.query;

import lombok.Data;

@Data
public class Daily {

    private String user;

    private String data;

    private String email;

    private Integer messageType;
}
