package com.fate.archer.cos.dao;

import com.fate.archer.cos.entity.DeployProcess;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author FanK
 */
public interface DeployProcessMapper extends BaseMapper<DeployProcess> {

    /**
     * 根据订单编号查询流程
     *
     * @param orderCode 订单编号
     * @return 流程
     */
    List<LinkedHashMap<String, Object>> deployProcessByOrder(@Param("orderCode") String orderCode);
}
