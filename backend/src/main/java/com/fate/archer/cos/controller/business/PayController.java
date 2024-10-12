package com.fate.archer.cos.controller.business;

import com.alipay.api.AlipayApiException;
import com.fate.archer.common.utils.R;
import com.fate.archer.cos.entity.dto.AlipayBean;
import com.fate.archer.cos.service.PayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cos/pay")
public class PayController {

    @Autowired
    private PayService payService;

    /**
     * 阿里支付
     * @param subject
     * @param body
     * @return
     * @throws AlipayApiException
     */
    @PostMapping(value = "/alipay")
    public R alipay(String outTradeNo, String subject, String totalAmount, String body) throws AlipayApiException {
        AlipayBean alipayBean = new AlipayBean();
        alipayBean.setOut_trade_no(outTradeNo);
        alipayBean.setSubject(subject);
        alipayBean.setTotal_amount(totalAmount);
        alipayBean.setBody(body);
        String result = payService.aliPay(alipayBean);
        return R.ok(result);
    }

}
