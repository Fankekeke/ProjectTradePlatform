package com.fate.archer.cos.entity.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author FanK
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ProductTradeWithSchoolVo implements Serializable {

    /**
     * 交易时间
     */
    private String tradeDate;

    /**
     * 学校名称
     */
    private String schoolName;

    /**
     * 账户名称
     */
    private String account;

    /**
     * 账户头像
     */
    private String headImgUrl;

    public ProductTradeWithSchoolVo(String schoolName, String tradeDate, String account, String headImgUrl) {
        this.tradeDate = tradeDate;
        this.schoolName = schoolName;
        this.account = account;
        this.headImgUrl = headImgUrl;
    }

    public ProductTradeWithSchoolVo() {}
}
