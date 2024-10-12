package com.fate.archer.cos.service.impl;


import com.alipay.api.AlipayApiException;
import com.fate.archer.common.utils.Alipay;
import com.fate.archer.cos.entity.dto.AlipayBean;
import com.fate.archer.cos.service.PayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PayServiceImpl implements PayService {

    @Autowired
    private Alipay alipay;

    @Override
    public String aliPay(AlipayBean alipayBean) throws AlipayApiException {
        return alipay.pay(alipayBean);
    }
}