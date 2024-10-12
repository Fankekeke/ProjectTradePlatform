package com.fate.archer.cos.service;

import com.fate.archer.cos.entity.CustomizePaymentProcess;
import com.baomidou.mybatisplus.extension.service.IService;

import java.math.BigDecimal;

/**
 * @author FanK
 */
public interface ICustomizePaymentProcessService extends IService<CustomizePaymentProcess> {

    /**
     * 定制项目添加支付流程
     *
     * @param customCode  定制项目编号
     * @param customPrice 项目价格
     * @param thesisPrice 论文价格
     * @return 返回结果
     */
    Boolean save(String customCode, BigDecimal customPrice, BigDecimal thesisPrice);

    /**
     * 修改定制项目流程
     *
     * @param customCode 定制项目编号
     * @param step       当前流程
     * @param payType
     * @return 支付方式
     */
    Boolean updateStatus(String customCode, Integer step, Integer payType);

    /**
     * 定制项目删除论文
     *
     * @param customCode 定制项目编号
     * @return 返回结果
     */
    Boolean removeThesis(String customCode);
}
