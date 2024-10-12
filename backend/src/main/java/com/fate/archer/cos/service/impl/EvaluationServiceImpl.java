package com.fate.archer.cos.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fate.archer.common.exception.FebsException;
import com.fate.archer.cos.entity.Evaluation;
import com.fate.archer.cos.dao.EvaluationMapper;
import com.fate.archer.cos.service.IEvaluationService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author FanK
 */
@Service
public class EvaluationServiceImpl extends ServiceImpl<EvaluationMapper, Evaluation> implements IEvaluationService {

    /**
     * 分页查询订单评价
     *
     * @param page       分页镀锡
     * @param evaluation 评价信息
     * @return 订单评价列表
     */
    @Override
    public IPage<LinkedHashMap<String, Object>> getEvaluationPage(Page page, Evaluation evaluation) {
        return baseMapper.getEvaluationPage(page, evaluation);
    }

    /**
     * 添加评价信息
     *
     * @param evaluation 评价信息
     * @return 结果
     */
    @Override
    public Boolean addEvaluation(Evaluation evaluation) {
        evaluation.setDate(DateUtil.formatDateTime(new Date()));
        return this.save(evaluation);
    }

    /**
     * 查询订单详情
     *
     * @param orderCode 订单编号
     * @return 订单详情
     * @throws Exception 异常
     */
    @Override
    public Evaluation selectEvaluationDetail(String orderCode) throws Exception {
        if (StrUtil.isBlank(orderCode)) {
            throw new FebsException("参数校验错误！");
        }
        return this.getOne(Wrappers.<Evaluation>lambdaQuery().eq(Evaluation::getOrderCode, orderCode).eq(Evaluation::getDeleteFlag, 0));
    }
}
