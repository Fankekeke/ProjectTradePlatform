package com.fate.archer.cos.service.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fate.archer.common.domain.BusinessConstant;
import com.fate.archer.common.exception.FebsException;
import com.fate.archer.cos.dao.TeamInfoMapper;
import com.fate.archer.cos.entity.TeamInfo;
import com.fate.archer.cos.entity.TeamTakeReview;
import com.fate.archer.cos.dao.TeamTakeReviewMapper;
import com.fate.archer.cos.service.ITeamInfoService;
import com.fate.archer.cos.service.ITeamTakeReviewService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author FanK
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TeamTakeReviewServiceImpl extends ServiceImpl<TeamTakeReviewMapper, TeamTakeReview> implements ITeamTakeReviewService {

    private final ITeamInfoService teamInfoService;

    /**
     * 查询开发者提现记录
     *
     * @param page           分页对象
     * @param teamTakeReview 开发者提现记录
     * @return 结果
     */
    @Override
    public IPage<TeamTakeReview> selectTeamTakeReviewPage(Page page, TeamTakeReview teamTakeReview) {
        return baseMapper.selectTeamTakeReviewPage(page, teamTakeReview);
    }

    /**
     * 开发者提现申请
     *
     * @param teamTakeReview 开发者提现申请
     * @return 结果
     * @throws Exception 异常
     */
    @Override
    public Boolean addTeamTakeReview(TeamTakeReview teamTakeReview) throws Exception {
        TeamInfo teamInfo = teamInfoService.getById(teamTakeReview.getTeamId());
        if (teamInfo == null) {
            throw new FebsException("开发者信息不存在！");
        }
        if (teamInfo.getBalance().compareTo(BusinessConstant.TASK_MONEY_STANDARD) > -1) {
            throw new FebsException("余额未达到提现标准！");
        }
        // 交易状态
        teamTakeReview.setReview(BusinessConstant.TAKE_MONEY_REVIEW_UNDER);
        // 交易金额
        teamTakeReview.setBalance(teamInfo.getBalance());
        // 申请时间
        teamTakeReview.setDate(DateUtil.formatDateTime(new Date()));
        return this.save(teamTakeReview);
    }

    /**
     * 修改开发者提现申请
     *
     * @param teamTakeReview 开发者提现申请
     * @return 结果
     * @throws Exception 异常
     */
    @Override
    public Boolean updateTeamTakeReview(TeamTakeReview teamTakeReview) throws Exception {
        // 交易完成
        if (teamTakeReview.getReview().equals(BusinessConstant.TAKE_MONEY_REVIEW_COMPLETE)) {
            teamTakeReview.setFinishDate(DateUtil.formatDateTime(new Date()));
            // 开发者余额清空
            teamInfoService.update(Wrappers.<TeamInfo>lambdaUpdate().set(TeamInfo::getBalance, BigDecimal.ZERO).eq(TeamInfo::getId, teamTakeReview.getTeamId()));
        }
        return this.updateById(teamTakeReview);
    }
}
