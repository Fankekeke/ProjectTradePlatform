package com.fate.archer.cos.controller;


import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.fate.archer.common.annotation.Log;
import com.fate.archer.common.utils.R;
import com.fate.archer.cos.entity.RateInfo;
import com.fate.archer.cos.service.IRateInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author FanK
 */
@RestController
@RequestMapping("/cos/rate-info")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RateInfoController {

    private final IRateInfoService rateInfoService;

    /**
     * 查询佣金分配比率
     *
     * @return 佣金分配比率
     */
    @Log("查询佣金分配比率")
    @GetMapping("/rate")
    public R rateData() {
        return R.ok(rateInfoService.rateData());
    }

    /**
     * 修改佣金分配比率
     *
     * @return 返回结果
     */
    @PutMapping
    public R rateEdit(RateInfo rateInfo) {
        return R.ok(rateInfoService.update(Wrappers.<RateInfo>lambdaUpdate().set(RateInfo::getRate, rateInfo.getRate())
                .eq(RateInfo::getRole, rateInfo.getRole()).eq(RateInfo::getFlag, rateInfo.getFlag()).eq(RateInfo::getIsInvite, rateInfo.getIsInvite())));
    }

    /**
     * 根据角色获取分配比例
     *
     * @param role 角色
     * @return 分配比例
     */
    @GetMapping("/getRateByRole")
    public R getRateByRole(@RequestParam String role) {
        return R.ok(rateInfoService.list(Wrappers.<RateInfo>lambdaQuery().eq(RateInfo::getRole, role)));
    }

}
