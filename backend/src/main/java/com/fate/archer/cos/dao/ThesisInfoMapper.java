package com.fate.archer.cos.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fate.archer.cos.entity.ThesisInfo;
import org.apache.ibatis.annotations.Param;

/**
 * @author FanK
 */
public interface ThesisInfoMapper extends BaseMapper<ThesisInfo> {

    /**
     * 论文定制分页查询
     *
     * @param page       分页对象
     * @param thesisInfo 定制信息
     * @return 结果
     */
    IPage<ThesisInfo> selectThesisInfoPage(Page page, @Param("thesisInfo") ThesisInfo thesisInfo);
}
