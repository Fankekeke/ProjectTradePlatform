package com.fate.archer.cos.service;

import com.fate.archer.cos.entity.DeployProcess;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author FanK
 */
public interface IDeployProcessService extends IService<DeployProcess> {

    /**
     * 订单创建后操作
     *
     * @param orderCode 订单编号
     */
    void deployProcessSave(String orderCode);

    /**
     * 订单状态更改
     *
     * @param orderCode 订单编号
     * @param status    状态
     */
    void deployProcessEdit(String orderCode, Integer status);

    /**
     * 根据订单编号查询流程
     *
     * @param orderCode 订单编号
     * @return 流程
     */
    List<LinkedHashMap<String, Object>> deployProcessByOrder(String orderCode);

    /**
     * 部署流程
     *
     * @param deployProcess 部署流程
     * @return 返回结果
     */
    Boolean deployUpload(DeployProcess deployProcess);
}
