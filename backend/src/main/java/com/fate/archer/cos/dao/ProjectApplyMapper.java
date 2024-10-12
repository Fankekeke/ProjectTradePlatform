package com.fate.archer.cos.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fate.archer.cos.entity.ProjectApply;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author FanK
 */
public interface ProjectApplyMapper extends BaseMapper<ProjectApply> {

    /**
     * 分页查询我的项目
     * @param page 分页对象
     * @param projectApply 项目信息
     * @return 我的项目列表
     */
    IPage<LinkedHashMap<String, Object>> projectApplyPage(Page page, @Param("projectApply") ProjectApply projectApply);
}
