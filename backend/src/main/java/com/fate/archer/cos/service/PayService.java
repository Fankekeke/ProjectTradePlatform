package com.fate.archer.cos.service;

import com.alipay.api.AlipayApiException;
import com.fate.archer.cos.entity.dto.AlipayBean;

public interface PayService {

    /**
     * 支付宝支付接口
     * @param alipayBean
     * @return
     * @throws AlipayApiException
     */
    String aliPay(AlipayBean alipayBean) throws AlipayApiException;
}
