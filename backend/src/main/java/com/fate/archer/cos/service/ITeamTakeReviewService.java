package com.fate.archer.cos.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fate.archer.cos.entity.TeamTakeReview;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author FanK
 */
public interface ITeamTakeReviewService extends IService<TeamTakeReview> {

    /**
     * 查询开发者提现记录
     *
     * @param page           分页对象
     * @param teamTakeReview 开发者提现记录
     * @return 结果
     */
    IPage<TeamTakeReview> selectTeamTakeReviewPage(Page page, TeamTakeReview teamTakeReview);

    /**
     * 开发者提现申请
     *
     * @param teamTakeReview 开发者提现申请
     * @return 结果
     * @throws Exception 异常
     */
    Boolean addTeamTakeReview(TeamTakeReview teamTakeReview) throws Exception;

    /**
     * 修改开发者提现申请
     *
     * @param teamTakeReview 开发者提现申请
     * @return 结果
     * @throws Exception 异常
     */
    Boolean updateTeamTakeReview(TeamTakeReview teamTakeReview) throws Exception;
}
