package com.fate.archer.cos.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fate.archer.cos.entity.Evaluation;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author FanK
 */
public interface EvaluationMapper extends BaseMapper<Evaluation> {

    /**
     * 分页查询订单评价
     *
     * @param page       分页对象
     * @param evaluation 订单评价
     * @return 订单评价列表
     */
    IPage<LinkedHashMap<String, Object>> getEvaluationPage(Page page, @Param("evaluation") Evaluation evaluation);
}
