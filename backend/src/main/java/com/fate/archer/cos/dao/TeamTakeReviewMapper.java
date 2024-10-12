package com.fate.archer.cos.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fate.archer.cos.entity.TeamTakeReview;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author FanK
 */
public interface TeamTakeReviewMapper extends BaseMapper<TeamTakeReview> {

    /**
     * 查询开发者提现记录
     *
     * @param page           分页对象
     * @param teamTakeReview 开发者提现记录
     * @return 结果
     */
    IPage<TeamTakeReview> selectTeamTakeReviewPage(Page page, @Param("teamTakeReview") TeamTakeReview teamTakeReview);
}
