package com.fate.archer.cos.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fate.archer.cos.entity.CustomApplication;
import com.fate.archer.cos.entity.CustomizeInfo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author FanK
 */
public interface ICustomizeInfoService extends IService<CustomizeInfo> {

    /**
     * 分页查询项目定制信息
     *
     * @param page          分页对象
     * @param customizeInfo 项目定制信息
     * @return 项目定制信息列表
     */
    IPage<LinkedHashMap<String, Object>> customizeInfoPage(Page page, CustomizeInfo customizeInfo);

    /**
     * 根据技术人员获取定制信息
     *
     * @param technician 人员信息
     * @param status     当前定制状态
     * @return 定制信息
     */
    List<LinkedHashMap<String, Object>> customizeInfoBuild(Integer technician, Integer status);

    /**
     * 订单审核通过资金分配
     *
     * @param orderCode 订单编号
     */
    void customizeAudit(String orderCode);

    /**
     * 定制申请生成
     *
     * @param customApplication 定制申请信息
     * @return 返回结果
     */
    Boolean applicationMade(CustomApplication customApplication);

    /**
     * 定制项目撤销论文模块
     *
     * @param customCode 定制编号
     * @return 返回结果
     */
    Boolean revokeThesis(String customCode);

    /**
     * 定制项目订单分配人员
     *
     * @param customCode 定制编号
     * @param teamIds    团队人员
     * @return 返回结果
     */
    Boolean assignment(String customCode, String teamIds);

    /**
     * 项目定制审核
     *
     * @param orderCode 定制编号
     * @param status    审核状态
     * @return 返回结果
     */
    Boolean customizeAudit(String orderCode, Integer status);


}
