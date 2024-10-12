package com.fate.archer.cos.service.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fate.archer.common.domain.BusinessConstant;
import com.fate.archer.common.exception.FebsException;
import com.fate.archer.cos.dao.UserInfoMapper;
import com.fate.archer.cos.entity.TakeReview;
import com.fate.archer.cos.dao.TakeReviewMapper;
import com.fate.archer.cos.entity.UserInfo;
import com.fate.archer.cos.service.ITakeReviewService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedHashMap;

/**
 * @author FanK
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TakeReviewServiceImpl extends ServiceImpl<TakeReviewMapper, TakeReview> implements ITakeReviewService {

    private final UserInfoMapper userInfoMapper;

    /**
     * 分页获取提现申请
     *
     * @param page       分页对象
     * @param takeReview 提现申请
     * @return 提现申请列表
     */
    @Override
    public IPage<LinkedHashMap<String, Object>> takeReviewPage(Page page, TakeReview takeReview) {
        return baseMapper.takeReviewPage(page, takeReview);
    }

    /**
     * 用户提现申请
     *
     * @param review 提现申请
     * @return 返回结果
     * @throws Exception 异常
     */
    @Override
    public Boolean takeReview(TakeReview review) throws Exception {
        UserInfo userInfo = userInfoMapper.selectOne(Wrappers.<UserInfo>lambdaQuery().eq(UserInfo::getCode, review.getUserCode()));
        if (userInfo == null) {
            throw new FebsException("用户信息不存在！");
        }
        if (userInfo.getBalance().compareTo(BusinessConstant.TASK_MONEY_STANDARD) > -1) {
            throw new FebsException("余额未达到提现标准！");
        }
        // 审理状态
        review.setReview(BusinessConstant.TAKE_MONEY_REVIEW_UNDER);
        // 交易日期
        review.setDate(DateUtil.formatDateTime(new Date()));
        // 交易金额
        review.setBalance(userInfo.getBalance());
        return this.save(review);
    }
}
