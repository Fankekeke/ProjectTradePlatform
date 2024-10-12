package com.fate.archer.cos.entity;

import lombok.Data;

@Data
public class WechatUserInfo {

    private String openId;

    private String nickName;

    private Integer sex;

    private String province;

    private String city;

    private String country;

    private String headImgUrl;
}
