package com.fate.archer.cos.controller;


import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.fate.archer.common.utils.R;
import com.fate.archer.cos.entity.CustomizeInfo;
import com.fate.archer.cos.entity.CustomizeLedger;
import com.fate.archer.cos.entity.TeamInfo;
import com.fate.archer.cos.service.ICustomizeInfoService;
import com.fate.archer.cos.service.ICustomizeLedgerService;
import com.fate.archer.cos.service.ITeamInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * @author FanK
 */
@RestController
@RequestMapping("/cos/customize-ledger")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CustomizeLedgerController {

    private final ICustomizeLedgerService customizeLedgerService;

    private final ICustomizeInfoService customizeInfoService;

    private final ITeamInfoService teamInfoService;

    /**
     * 根据定制编号获取定制台账
     *
     * @param code 定制编号
     * @return 定制台账信息
     */
    @GetMapping("/ledger")
    public R customLedger(@RequestParam("code") String code) {
        return R.ok(customizeLedgerService.customLedger(code));
    }

    /**
     * 定制项目完成提交
     *
     * @param customizeLedger 项目台账信息
     * @return 返回结果
     */
    @PostMapping("/finish")
    public R customizeFinish(CustomizeLedger customizeLedger) {
        TeamInfo teamInfo = teamInfoService.getOne(Wrappers.<TeamInfo>lambdaQuery().eq(TeamInfo::getAdminUser, customizeLedger.getProposer()));
        customizeLedger.setProposer(teamInfo.getId());
        // 修改定制状态
        customizeInfoService.update(Wrappers.<CustomizeInfo>lambdaUpdate().set(CustomizeInfo::getStatus, 3).eq(CustomizeInfo::getCode, customizeLedger.getCustomizeCode()));
        customizeLedger.setCompleteDate(DateUtil.formatDateTime(new Date()));
        return R.ok(customizeLedgerService.save(customizeLedger));
    }

    /**
     * 制项目部署完成提交
     *
     * @param customizeLedger 项目台账信息
     * @return 返回结果
     */
    @PostMapping("/complete")
    public R customizeComplete(CustomizeLedger customizeLedger) {
        TeamInfo teamInfo = teamInfoService.getOne(Wrappers.<TeamInfo>lambdaQuery().eq(TeamInfo::getAdminUser, customizeLedger.getDeployUser()));
        customizeLedger.setDeployUser(teamInfo.getId());
        // 修改定制状态
        customizeInfoService.update(Wrappers.<CustomizeInfo>lambdaUpdate().set(CustomizeInfo::getStatus, 4).eq(CustomizeInfo::getCode, customizeLedger.getCustomizeCode()));
        return R.ok(customizeLedgerService.update(Wrappers.<CustomizeLedger>lambdaUpdate().set(CustomizeLedger::getDeployPic, customizeLedger.getDeployPic())
                .set(CustomizeLedger::getDeployDate, DateUtil.formatDateTime(new Date())).set(CustomizeLedger::getDeployUser, teamInfo.getId()).eq(CustomizeLedger::getCustomizeCode, customizeLedger.getCustomizeCode())));
    }

}
