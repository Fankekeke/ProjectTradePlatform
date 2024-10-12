package com.fate.archer.cos.controller;


import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fate.archer.common.annotation.Log;
import com.fate.archer.common.utils.R;
import com.fate.archer.cos.entity.DeployEvaluate;
import com.fate.archer.cos.entity.Evaluation;
import com.fate.archer.cos.service.IDeployEvaluateService;
import com.fate.archer.cos.service.IDeployProcessService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * @author FanK
 */
@RestController
@RequestMapping("/cos/deploy-evaluate")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DeployEvaluateController {

    private final IDeployEvaluateService deployEvaluateService;

    private final IDeployProcessService deployProcessService;

    /**
     * 部署审核
     *
     * @param deployEvaluate 部署审核信息
     * @return 审核结果
     */
    @Log("部署审核")
    @PostMapping("/audit")
    public R audit(DeployEvaluate deployEvaluate) {
        return R.ok(deployEvaluateService.audit(deployEvaluate));
    }

}
