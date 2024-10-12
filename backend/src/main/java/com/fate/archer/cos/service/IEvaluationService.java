package com.fate.archer.cos.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fate.archer.cos.entity.Evaluation;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author FanK
 */
public interface IEvaluationService extends IService<Evaluation> {

    /**
     * 分页查询订单评价
     *
     * @param page       分页对象
     * @param evaluation 评价信息
     * @return 订单评价列表
     */
    IPage<LinkedHashMap<String, Object>> getEvaluationPage(Page page, Evaluation evaluation);

    /**
     * 添加评价信息
     *
     * @param evaluation 评价信息
     * @return 结果
     */
    Boolean addEvaluation(Evaluation evaluation);

    /**
     * 查询订单详情
     *
     * @param orderCode 订单编号
     * @return 订单详情
     * @throws Exception 异常
     */
    Evaluation selectEvaluationDetail(String orderCode) throws Exception;
}
