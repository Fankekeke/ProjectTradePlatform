package com.fate.archer.cos.controller;


import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.fate.archer.common.annotation.Log;
import com.fate.archer.common.utils.R;
import com.fate.archer.cos.entity.TakeMoney;
import com.fate.archer.cos.entity.TakeReview;
import com.fate.archer.cos.entity.UserInfo;
import com.fate.archer.cos.service.ITakeMoneyService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fate.archer.cos.service.ITakeReviewService;
import com.fate.archer.cos.service.IUserInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * @author FanK
 */
@RestController
@RequestMapping("/cos/take-review")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TakeReviewController {

    private final ITakeReviewService takeReviewService;

    private final IUserInfoService userInfoService;

    /**
     * 查询提现申请
     *
     * @param page       分页对象
     * @param takeReview 提现申请
     * @return 提现申请列表
     */
    @Log("查询提现申请")
    @GetMapping("/page")
    public R page(Page<TakeReview> page, TakeReview takeReview) {
        return R.ok(takeReviewService.takeReviewPage(page, takeReview));
    }

    /**
     * 新增提现申请
     *
     * @param takeReview 提现申请
     * @return 返回结果
     */
    @Log("新增提现申请")
    @PostMapping
    public R save(TakeReview takeReview) throws Exception {
        return R.ok(takeReviewService.takeReview(takeReview));
    }

    /**
     * 修改提现申请
     *
     * @param takeReview 提现申请
     * @return 返回结果
     */
    @Log("修改提现申请")
    @PutMapping
    @Transactional(rollbackFor = Exception.class)
    public R edit(TakeReview takeReview) {
        if (takeReview.getReview() == 3) {
            userInfoService.balanceClean(takeReview.getUserCode());
        }
        return R.ok(takeReviewService.updateById(takeReview));
    }

    /**
     * 删除提现申请
     *
     * @param ids 提现申请Id
     * @return 返回结果
     */
    @Log("删除提现申请")
    @DeleteMapping("/{ids}")
    public R delete(@PathVariable("ids") List<Integer> ids) {
        return R.ok(takeReviewService.removeByIds(ids));
    }

}
