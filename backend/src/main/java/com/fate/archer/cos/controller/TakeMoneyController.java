package com.fate.archer.cos.controller;


import com.fate.archer.common.annotation.Log;
import com.fate.archer.common.utils.R;
import com.fate.archer.cos.entity.TakeMoney;
import com.fate.archer.cos.service.ITakeMoneyService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author FanK
 */
@RestController
@RequestMapping("/cos/take-money")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TakeMoneyController {

    private final ITakeMoneyService takeMoneyService;

    /**
     * 查询提现记录
     *
     * @param page      分页对象
     * @param takeMoney 提现记录
     * @return 提现记录列表
     */
    @Log("查询提现记录")
    @GetMapping("/page")
    public R page(Page<TakeMoney> page, TakeMoney takeMoney) {
        return R.ok();
    }

    /**
     * 新增提现记录
     *
     * @param takeMoney 提现记录
     * @return 返回结果
     */
    @Log("新增提现记录")
    @PostMapping
    public R save(TakeMoney takeMoney) {
        return R.ok(takeMoneyService.save(takeMoney));
    }

    /**
     * 修改提现记录
     *
     * @param takeMoney 提现记录
     * @return 返回结果
     */
    @Log("修改提现记录")
    @PutMapping
    public R edit(TakeMoney takeMoney) {
        return R.ok(takeMoneyService.updateById(takeMoney));
    }

    /**
     * 删除提现记录
     *
     * @param ids 提现记录Id
     * @return 返回结果
     */
    @Log("删除提现记录")
    @DeleteMapping("/{ids}")
    public R delete(@PathVariable("ids") List<Integer> ids) {
        return R.ok();
    }

}
