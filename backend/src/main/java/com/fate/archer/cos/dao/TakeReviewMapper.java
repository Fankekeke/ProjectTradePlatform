package com.fate.archer.cos.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fate.archer.cos.entity.TakeReview;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;

/**
 * @author FanK
 */
public interface TakeReviewMapper extends BaseMapper<TakeReview> {

    /**
     * 分页获取提现申请
     * @param page 分页对象
     * @param takeReview 提现申请
     * @return 提现申请列表
     */
    IPage<LinkedHashMap<String, Object>> takeReviewPage(Page page, @Param("takeReview") TakeReview takeReview);
}
