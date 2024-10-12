package com.fate.archer.cos.service.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.fate.archer.common.utils.R;
import com.fate.archer.cos.entity.DeployProcess;
import com.fate.archer.cos.dao.DeployProcessMapper;
import com.fate.archer.cos.service.IDeployProcessService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author FanK
 */
@Service
public class DeployProcessServiceImpl extends ServiceImpl<DeployProcessMapper, DeployProcess> implements IDeployProcessService {

    /**
     * 订单创建后操作
     *
     * @param orderCode 订单编号
     */
    @Override
    public void deployProcessSave(String orderCode) {
        List<DeployProcess> deployProcessList = new ArrayList<>();
        deployProcessList.add(new DeployProcess(orderCode, 1, DateUtil.formatDateTime(new Date()), 1));
        deployProcessList.add(new DeployProcess(orderCode, 2, null, 0));
        deployProcessList.add(new DeployProcess(orderCode, 3, null, 0));
        deployProcessList.add(new DeployProcess(orderCode, 4, null, 0));
        this.saveBatch(deployProcessList);
    }

    /**
     * 订单状态更改
     *
     * @param orderCode 订单编号
     * @param status    状态
     */
    @Override
    public void deployProcessEdit(String orderCode, Integer status) {
        // 状态清除
        this.update(Wrappers.<DeployProcess>lambdaUpdate().set(DeployProcess::getCurrentStatus, 0).eq(DeployProcess::getOrderCode, orderCode));
        DeployProcess deployProcess = this.getOne(Wrappers.<DeployProcess>lambdaQuery().eq(DeployProcess::getOrderCode, orderCode).eq(DeployProcess::getStatus, status));
        deployProcess.setOperateTime(DateUtil.formatDateTime(new Date()));
        deployProcess.setCurrentStatus(1);
        this.updateById(deployProcess);
    }

    /**
     * 根据订单编号查询流程
     *
     * @param orderCode 订单编号
     * @return 流程
     */
    @Override
    public List<LinkedHashMap<String, Object>> deployProcessByOrder(String orderCode) {
        return baseMapper.deployProcessByOrder(orderCode);
    }

    /**
     * 部署流程
     *
     * @param deployProcess 部署流程
     * @return 返回结果
     */
    @Override
    public Boolean deployUpload(DeployProcess deployProcess) {
        // 判断订单号是否存在
        int count = this.count(Wrappers.<DeployProcess>lambdaQuery().eq(DeployProcess::getOrderCode, deployProcess.getOrderCode()));
        if (count == 0) {
            // 重新添加部署流程
            List<DeployProcess> deployProcessList = new ArrayList<>();
            deployProcessList.add(new DeployProcess(deployProcess.getOrderCode(), 1, DateUtil.formatDateTime(new Date()), 0));
            deployProcessList.add(new DeployProcess(deployProcess.getOrderCode(), 2, DateUtil.formatDateTime(new Date()), 1));
            deployProcessList.add(new DeployProcess(deployProcess.getOrderCode(), 3, null, 0));
            deployProcessList.add(new DeployProcess(deployProcess.getOrderCode(), 4, null, 0));
            this.saveBatch(deployProcessList);
        } else {
            // 清除状态
            this.update(Wrappers.<DeployProcess>lambdaUpdate().set(DeployProcess::getCurrentStatus, 0).eq(DeployProcess::getOrderCode, deployProcess.getOrderCode()));
        }
        return this.update(Wrappers.<DeployProcess>lambdaUpdate().set(DeployProcess::getCurrentStatus, 1)
                .set(DeployProcess::getOperatePic, deployProcess.getOperatePic()).set(DeployProcess::getOperateTime, DateUtil.formatDateTime(new Date()))
                .eq(DeployProcess::getOrderCode, deployProcess.getOrderCode()).eq(DeployProcess::getStatus, 2));
    }
}
