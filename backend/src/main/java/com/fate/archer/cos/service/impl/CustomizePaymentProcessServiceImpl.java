package com.fate.archer.cos.service.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.fate.archer.common.domain.BusinessConstant;
import com.fate.archer.cos.dao.CustomizeInfoMapper;
import com.fate.archer.cos.entity.CustomizeInfo;
import com.fate.archer.cos.entity.CustomizePaymentProcess;
import com.fate.archer.cos.dao.CustomizePaymentProcessMapper;
import com.fate.archer.cos.entity.ThesisInfo;
import com.fate.archer.cos.service.ICustomizeInfoService;
import com.fate.archer.cos.service.ICustomizePaymentProcessService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fate.archer.cos.service.IThesisInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author FanK
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CustomizePaymentProcessServiceImpl extends ServiceImpl<CustomizePaymentProcessMapper, CustomizePaymentProcess> implements ICustomizePaymentProcessService {

    private final CustomizeInfoMapper customizeInfoMapper;

    @Override
    public Boolean save(String customCode, BigDecimal customPrice, BigDecimal thesisPrice) {
        CustomizePaymentProcess customizePaymentProcessc = new CustomizePaymentProcess();
        customizePaymentProcessc.setCustomCode(customCode);
        // 定金计算
        customizePaymentProcessc.setFrontMoney(customPrice.add(thesisPrice).multiply(BusinessConstant.DEPOSIT_RATIO.multiply(new BigDecimal("0.01"))));
        customizePaymentProcessc.setFrontStatus(0);
        // 尾款计算
        customizePaymentProcessc.setFinalMoney(customPrice.add(thesisPrice).subtract(customizePaymentProcessc.getFrontMoney()));
        customizePaymentProcessc.setFinalStatus(0);
        return this.save(customizePaymentProcessc);
    }

    @Override
    public Boolean updateStatus(String customCode, Integer step, Integer payType) {
        CustomizePaymentProcess customizePaymentProcess = this.getOne(Wrappers.<CustomizePaymentProcess>lambdaQuery().eq(CustomizePaymentProcess::getCustomCode, customCode));
        if (step == 1) {
            customizePaymentProcess.setFrontStatus(1);
            customizePaymentProcess.setFrontType(payType);
            customizePaymentProcess.setFrontDate(DateUtil.formatDateTime(new Date()));
        } else if (step == 2) {
            customizePaymentProcess.setFinalStatus(1);
            customizePaymentProcess.setFinalType(payType);
            customizePaymentProcess.setFinalDate(DateUtil.formatDateTime(new Date()));
        }
        return this.updateById(customizePaymentProcess);
    }

    @Override
    public Boolean removeThesis(String customCode) {
        // 获取定制信息
        CustomizeInfo customizeInfo = customizeInfoMapper.selectOne(Wrappers.<CustomizeInfo>lambdaQuery().eq(CustomizeInfo::getCode, customCode));
        // 获取支付流程
        CustomizePaymentProcess customizePaymentProcess = this.getOne(Wrappers.<CustomizePaymentProcess>lambdaQuery().eq(CustomizePaymentProcess::getCustomCode, customCode));
        // 计算价格
        if (customizePaymentProcess.getFrontStatus() == 0) {
            // 未支付定金-修改定金金额以及尾款金额
            BigDecimal frontMoney = customizeInfo.getAmount().multiply(BusinessConstant.DEPOSIT_RATIO.multiply(new BigDecimal("0.01")));
            BigDecimal finalMoney = customizeInfo.getAmount().subtract(frontMoney);
            return this.update(Wrappers.<CustomizePaymentProcess>lambdaUpdate().set(CustomizePaymentProcess::getFrontMoney, frontMoney)
                    .set(CustomizePaymentProcess::getFinalMoney, finalMoney).eq(CustomizePaymentProcess::getCustomCode, customCode));
        } else {
            // 已支付定金-修改尾款金额
            BigDecimal finalMoney = customizeInfo.getAmount().subtract(customizePaymentProcess.getFrontMoney());
            return this.update(Wrappers.<CustomizePaymentProcess>lambdaUpdate().set(CustomizePaymentProcess::getFinalMoney, finalMoney)
                    .eq(CustomizePaymentProcess::getCustomCode, customCode));
        }
    }
}
