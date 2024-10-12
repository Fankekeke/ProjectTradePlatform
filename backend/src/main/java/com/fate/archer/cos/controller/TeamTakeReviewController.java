package com.fate.archer.cos.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fate.archer.common.utils.R;
import com.fate.archer.cos.entity.TeamTakeReview;
import com.fate.archer.cos.service.ITeamTakeReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author FanK
 */
@RestController
@RequestMapping("/cos/team-take-review")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TeamTakeReviewController {

    private final ITeamTakeReviewService teamTakeReviewService;

    /**
     * 查询开发者提现记录
     *
     * @param page           分页对象
     * @param teamTakeReview 开发者提现记录
     * @return
     */
    @GetMapping("/page")
    public R page(Page page, TeamTakeReview teamTakeReview) {
        return R.ok(teamTakeReviewService.selectTeamTakeReviewPage(page, teamTakeReview));
    }

    /**
     * 开发者提现申请
     *
     * @param teamTakeReview 开发者提现申请
     * @return 结果
     */
    @PostMapping
    public R save(TeamTakeReview teamTakeReview) throws Exception {
        return R.ok(teamTakeReviewService.addTeamTakeReview(teamTakeReview));
    }

    /**
     * 修改开发者提现申请
     *
     * @param teamTakeReview 开发者提现申请
     * @throws Exception 结果
     */
    @PutMapping
    public R edit(TeamTakeReview teamTakeReview) throws Exception {
        return R.ok(teamTakeReviewService.updateTeamTakeReview(teamTakeReview));
    }

    /**
     * 删除开发者提现申请
     *
     * @param ids 开发者提现申请ID
     * @return 结果
     */
    @DeleteMapping("/{ids}")
    public R deleteIds(@PathVariable("ids") List<Integer> ids) {
        return R.ok(teamTakeReviewService.removeByIds(ids));
    }

}
