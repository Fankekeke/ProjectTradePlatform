package com.fate.archer.cos.controller.business;

import com.alipay.api.AlipayApiException;
import com.fate.archer.common.utils.R;
import com.fate.archer.cos.entity.dto.AlipayBean;
import com.fate.archer.cos.service.PayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 支付操作
 *
 * @Author FanK
 * @Date 2024-10-11 09:21:29
 * @Copyright 橘子森林
 */
@RestController
@RequestMapping("/cos/pay")
public class PayController {

    @Resource
    private PayService payService;

    /**
     * 支付宝沙盒支付
     *
     * @param outTradeNo  订单号
     * @param subject     账目标注
     * @param totalAmount 总价格
     * @param body        备注
     * @return 结果
     * @throws AlipayApiException 异常
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
