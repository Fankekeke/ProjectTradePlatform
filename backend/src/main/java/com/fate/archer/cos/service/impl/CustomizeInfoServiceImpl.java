package com.fate.archer.cos.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fate.archer.common.domain.BusinessConstant;
import com.fate.archer.common.utils.R;
import com.fate.archer.cos.entity.*;
import com.fate.archer.cos.dao.CustomizeInfoMapper;
import com.fate.archer.cos.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;

/**
 * @author FanK
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CustomizeInfoServiceImpl extends ServiceImpl<CustomizeInfoMapper, CustomizeInfo> implements ICustomizeInfoService {

    private final IUserInfoService userInfoService;

    private final IThesisInfoService thesisInfoService;

    private final ICustomApplicationService customApplicationService;

    private final ICustomizePaymentProcessService customizePaymentProcessService;

    private final IWealthAssignService wealthAssignService;

    @Override
    public IPage<LinkedHashMap<String, Object>> customizeInfoPage(Page page, CustomizeInfo customizeInfo) {
        return baseMapper.customizeInfoPage(page, customizeInfo);
    }

    @Override
    public List<LinkedHashMap<String, Object>> customizeInfoBuild(Integer technician, Integer status) {
        return baseMapper.customizeInfoBuild(technician, status);
    }

    @Override
    public void customizeAudit(String orderCode) {
        WealthAssign wealthAssign = new WealthAssign();
        wealthAssign.setOrderCode(orderCode);
        // 审核通过
        CustomizeInfo customizeInfo = baseMapper.selectOne(Wrappers.<CustomizeInfo>lambdaQuery().eq(CustomizeInfo::getCode, orderCode));
        // 获取开发者账户
        String[] technicians = customizeInfo.getTechnician().split(",");
        for (int i = 0; i < technicians.length; i++) {
            if (i == 0) {
                wealthAssign.setDevelopera(Integer.parseInt(technicians[i]));
            }
            if (i == 1) {
                wealthAssign.setDeveloperb(Integer.parseInt(technicians[i]));
            }
            if (i == 2) {
                wealthAssign.setDeveloperc(Integer.parseInt(technicians[i]));
            }
        }
        // 是否存在邀请人
        UserInfo userInfo = userInfoService.getOne(Wrappers.<UserInfo>lambdaQuery().eq(UserInfo::getCode, customizeInfo.getUserCode()));
        if (!StrUtil.isBlank(userInfo.getInviteUser())) {
            wealthAssign.setInviteCode(userInfo.getInviteUser());
        }
        // 订单金额（项目+论文）
        if (!StrUtil.isBlank(customizeInfo.getThesisCode())) {
            ThesisInfo thesisInfo = thesisInfoService.getOne(Wrappers.<ThesisInfo>lambdaQuery().eq(ThesisInfo::getCode, customizeInfo.getThesisCode()));
            wealthAssign.setWealth(customizeInfo.getAmount().add(thesisInfo.getPrice()));
        } else {
            wealthAssign.setWealth(customizeInfo.getAmount());
        }
    }

    @Override
    public Boolean applicationMade(CustomApplication customApplication) {
        // 添加项目定制
        CustomizeInfo customizeInfo = new CustomizeInfo();
        customizeInfo.setCode("CUS-" + System.currentTimeMillis());
        customizeInfo.setProName(customApplication.getProName());
        customizeInfo.setModule(customApplication.getModule());
        customizeInfo.setSkills(customApplication.getSkills());
        customizeInfo.setUserCode(customApplication.getUserCode());
        customizeInfo.setAmount(customApplication.getAmount());
        // 默认生成订单为待审核
        customizeInfo.setStatus(BusinessConstant.CUSTOM_STATUS_RECEIVED);
        customizeInfo.setContact(customApplication.getContact());
        customizeInfo.setCycle(customApplication.getCycle());
        customizeInfo.setRemarks(customApplication.getRemarks());
        customizeInfo.setStartDate(customApplication.getStartDate());
        // 判断是否需要添加论文
        ThesisInfo thesisInfo = new ThesisInfo();
        if (customApplication.getThesis() != null && Objects.equals(customApplication.getThesis(), BusinessConstant.CUSTOMIZATION_THESIS_YES)) {
            thesisInfo.setCode("THESIS-" + System.currentTimeMillis());
            thesisInfo.setThesisName(customApplication.getThesisName());
            thesisInfo.setWordCount(customApplication.getWordCount());
            thesisInfo.setDuplicate(customApplication.getDuplicate());
            thesisInfo.setPrice(customApplication.getPrice());
            thesisInfo.setCycle(customApplication.getThesisCycle());
            thesisInfo.setTechnician(customApplication.getTechnician());
            // 论文项目绑定
            customizeInfo.setThesisCode(thesisInfo.getCode());
            // 定金计算
            customizeInfo.setDeposit(customApplication.getAmount().add(customApplication.getPrice()).multiply(BusinessConstant.DEPOSIT_RATIO.multiply(new BigDecimal("0.01"))));
            // 添加论文信息
            thesisInfoService.save(thesisInfo);
        }
        // 生成定制项目
        this.save(customizeInfo);
        customizePaymentProcessService.save(customizeInfo.getCode(), customizeInfo.getAmount(), thesisInfo.getPrice());
        // 修改定制申请状态
        return customApplicationService.update(Wrappers.<CustomApplication>lambdaUpdate().set(CustomApplication::getCustomizeCode, customizeInfo.getCode())
                .eq(CustomApplication::getCode, customApplication.getCode()));
    }

    @Override
    public Boolean revokeThesis(String customCode) {
        try {
            // 获取定制论文编号
            String thesisCode = this.getOne(Wrappers.<CustomizeInfo>lambdaQuery().eq(CustomizeInfo::getCode, customCode)).getThesisCode();
            // 修改项目绑定状态
            this.update(Wrappers.<CustomizeInfo>lambdaUpdate().set(CustomizeInfo::getThesisCode, null).eq(CustomizeInfo::getCode, customCode));
            // 删除论文分配人
            thesisInfoService.update(Wrappers.<ThesisInfo>lambdaUpdate().set(ThesisInfo::getTechnician, null).eq(ThesisInfo::getCode, thesisCode));
            customizePaymentProcessService.removeThesis(customCode);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public Boolean assignment(String customCode, String teamIds) {
        return this.update(Wrappers.<CustomizeInfo>lambdaUpdate().set(CustomizeInfo::getTechnician, teamIds).eq(CustomizeInfo::getCode, customCode));
    }

    @Override
    public Boolean customizeAudit(String orderCode, Integer status) {
        // 项目审核完成
        if (status.equals(BusinessConstant.CUSTOM_STATUS_REVIEW_COMPLETED)) {
            WealthAssign wealthAssign = new WealthAssign();
            wealthAssign.setOrderCode(orderCode);
            // 获取定制信息
            CustomizeInfo customizeInfo = this.getOne(Wrappers.<CustomizeInfo>lambdaQuery().eq(CustomizeInfo::getCode, orderCode));
            // 获取开发者账户
            String[] technicians = customizeInfo.getTechnician().split(",");
            for (int i = 0; i < technicians.length; i++) {
                if (i == 0) {
                    wealthAssign.setDevelopera(Integer.parseInt(technicians[0]));
                }
                if (i == 1) {
                    wealthAssign.setDeveloperb(Integer.parseInt(technicians[1]));
                }
                if (i == 2) {
                    wealthAssign.setDeveloperc(Integer.parseInt(technicians[2]));
                }
            }
            wealthAssign.setWealth(customizeInfo.getAmount());
            // 是否存在邀请人
            UserInfo userInfo = userInfoService.getOne(Wrappers.<UserInfo>lambdaQuery().eq(UserInfo::getCode, customizeInfo.getUserCode()));
            if (!StrUtil.isBlank(userInfo.getInviteUser())) {
                wealthAssign.setInviteCode(userInfo.getInviteUser());
            }
            wealthAssignService.wealthAssignCustomize(wealthAssign);
        }
        return this.update(Wrappers.<CustomizeInfo>lambdaUpdate().set(CustomizeInfo::getStatus, status).eq(CustomizeInfo::getCode, orderCode));
    }
}
