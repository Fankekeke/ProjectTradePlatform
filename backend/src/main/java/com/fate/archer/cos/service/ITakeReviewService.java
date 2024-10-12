package com.fate.archer.cos.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fate.archer.cos.entity.TakeReview;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;

/**
 * @author FanK
 */
public interface ITakeReviewService extends IService<TakeReview> {

    /**
     * 分页获取提现申请
     *
     * @param page       分页对象
     * @param takeReview 提现申请
     * @return 提现申请列表
     */
    IPage<LinkedHashMap<String, Object>> takeReviewPage(Page page, TakeReview takeReview);

    /**
     * 用户提现申请
     *
     * @param review 提现申请
     * @return 返回结果
     * @throws Exception 异常
     */
    Boolean takeReview(TakeReview review) throws Exception;
}
