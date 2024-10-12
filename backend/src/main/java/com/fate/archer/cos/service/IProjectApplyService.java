package com.fate.archer.cos.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fate.archer.cos.entity.ProjectApply;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.LinkedHashMap;

/**
 * @author FanK
 */
public interface IProjectApplyService extends IService<ProjectApply> {

    /**
     * 分页查询我的项目
     * @param page 分页对象
     * @param projectApply 我的项目
     * @return 我的项目
     */
    IPage<LinkedHashMap<String, Object>> projectApplyPage(Page page, ProjectApply projectApply);

    /**
     * 生成项目信息
     * @param projectApply 我的项目
     * @param price 项目价格
     */
    void buildTheProject(ProjectApply projectApply, BigDecimal price);

}
