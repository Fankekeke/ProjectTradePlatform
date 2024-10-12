package com.fate.archer.cos.service;

import com.fate.archer.cos.entity.DeployEvaluate;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author FanK
 */
public interface IDeployEvaluateService extends IService<DeployEvaluate> {

    /**
     * 部署流程审核
     *
     * @param deployEvaluate 部署审核
     * @return 返回结果
     */
    Boolean audit(DeployEvaluate deployEvaluate);
}
