package com.fate.archer.cos.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fate.archer.common.annotation.Log;
import com.fate.archer.common.utils.R;
import com.fate.archer.cos.entity.Evaluation;
import com.fate.archer.cos.service.IEvaluationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author FanK
 */
@RestController
@RequestMapping("/cos/evaluation")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EvaluationController {

    private final IEvaluationService evaluationService;

    /**
     * 分页查询订单评价
     *
     * @param page       分页对象
     * @param evaluation 评价信息
     * @return
     */
    @Log("分页查询订单评价")
    @GetMapping("/page")
    public R page(Page page, Evaluation evaluation) {
        return R.ok(evaluationService.getEvaluationPage(page, evaluation));
    }

    /**
     * 删除订单评价
     *
     * @param ids 评价Id
     * @return 返回结果
     */
    @Log("删除订单评价")
    @DeleteMapping("/{ids}")
    public R deleteByIds(@PathVariable("ids") List<Integer> ids) {
        return R.ok(evaluationService.removeByIds(ids));
    }

    /**
     * 成品订单评价
     *
     * @param evaluation 评价信息
     * @return 结果
     */
    @PostMapping
    public R save(Evaluation evaluation) {
        return R.ok(evaluationService.addEvaluation(evaluation));
    }

    /**
     * 订单评价详情
     *
     * @param orderCode 订单编号
     * @return 评价详情
     */
    @GetMapping("/detail/{orderCode}")
    public R detail(@PathVariable("orderCode") String orderCode) throws Exception {
        return R.ok(evaluationService.selectEvaluationDetail(orderCode));
    }

}
