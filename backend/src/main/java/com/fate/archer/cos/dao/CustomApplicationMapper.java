package com.fate.archer.cos.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fate.archer.cos.entity.CustomApplication;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;

/**
 * @author FanK
 */
public interface CustomApplicationMapper extends BaseMapper<CustomApplication> {

    /**
     * 定制申请分页查询
     * @param page 分页对象
     * @param customApplication 定制申请
     * @return 定制申请列表
     */
    IPage<LinkedHashMap<String, Object>> customApplicationPage(Page page, @Param("customApplication") CustomApplication customApplication);
}
