package com.fate.archer.cos.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.fate.archer.common.domain.BusinessConstant;
import com.fate.archer.common.utils.R;
import com.fate.archer.cos.dao.OrderInfoMapper;
import com.fate.archer.cos.entity.*;
import com.fate.archer.cos.dao.DeployEvaluateMapper;
import com.fate.archer.cos.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * @author FanK
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DeployEvaluateServiceImpl extends ServiceImpl<DeployEvaluateMapper, DeployEvaluate> implements IDeployEvaluateService {

    private final IDeployProcessService deployProcessService;

    private final OrderInfoMapper orderInfoMapper;

    private final IProjectInfoService projectInfoService;

    private final IUserInfoService userInfoService;

    private final IWealthAssignService wealthAssignService;

    @Override
    public Boolean audit(DeployEvaluate deployEvaluate) {
        // 是否存在审核
        DeployEvaluate deployEvaluateExist = this.getOne(Wrappers.<DeployEvaluate>lambdaQuery().eq(DeployEvaluate::getOrderCode, deployEvaluate.getOrderCode()));
        if (deployEvaluate.getStatus() == 3) {
            WealthAssign wealthAssign = new WealthAssign();
            wealthAssign.setOrderCode(deployEvaluate.getOrderCode());
            // 审核通过
            OrderInfo orderInfo = orderInfoMapper.selectOne(Wrappers.<OrderInfo>lambdaQuery().eq(OrderInfo::getCode, deployEvaluate.getOrderCode()));
            // 获取开发者账户
            ProjectInfo projectInfo = projectInfoService.getOne(Wrappers.<ProjectInfo>lambdaQuery().eq(ProjectInfo::getCode, orderInfo.getProCode()));
            wealthAssign.setWealth(orderInfo.getNeedDeploy() == 1 ? projectInfo.getProPrice().add(BusinessConstant.NEED_DEPLOY_PRICE) : projectInfo.getProPrice());
            wealthAssign.setDevelopera(projectInfo.getAuthor());
            // 是否存在邀请人
            UserInfo userInfo = userInfoService.getOne(Wrappers.<UserInfo>lambdaQuery().eq(UserInfo::getCode, orderInfo.getUserCode()));
            if (!StrUtil.isBlank(userInfo.getInviteUser())) {
                wealthAssign.setInviteCode(userInfo.getInviteUser());
            }
            wealthAssignService.wealthAssign(wealthAssign);
        }
        // 修改审核流程
        deployProcessService.deployProcessEdit(deployEvaluate.getOrderCode(), deployEvaluate.getStatus());
        if (deployEvaluateExist != null) {
            return this.updateById(deployEvaluateExist);
        } else {
            return this.save(deployEvaluate);
        }
    }
}
