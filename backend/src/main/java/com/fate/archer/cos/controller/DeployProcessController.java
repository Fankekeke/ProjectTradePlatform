package com.fate.archer.cos.controller;


import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.fate.archer.common.utils.R;
import com.fate.archer.cos.entity.DeployProcess;
import com.fate.archer.cos.service.IDeployProcessService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author FanK
 */
@RestController
@RequestMapping("/cos/deploy-process")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DeployProcessController {

    private final IDeployProcessService deployProcessService;

    /**
     * 部署流程
     *
     * @param deployProcess 部署流程
     * @return 返回结果
     */
    @PostMapping("/deploy")
    public R deployUpload(DeployProcess deployProcess) {
        return R.ok(deployProcessService.deployUpload(deployProcess));
    }

}
