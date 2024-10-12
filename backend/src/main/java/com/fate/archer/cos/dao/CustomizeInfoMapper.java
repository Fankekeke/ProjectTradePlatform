package com.fate.archer.cos.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fate.archer.cos.entity.CustomizeInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author FanK
 */
public interface CustomizeInfoMapper extends BaseMapper<CustomizeInfo> {

    /**
     * 分页查询定制信息
     *
     * @param page          分页对象
     * @param customizeInfo 定制信息
     * @return 定制信息列表
     */
    IPage<LinkedHashMap<String, Object>> customizeInfoPage(Page page, @Param("customizeInfo") CustomizeInfo customizeInfo);

    /**
     * 根据技术人员获取定制信息
     *
     * @param technician 开发人员
     * @param status     状态
     * @return 定制信息
     */
    List<LinkedHashMap<String, Object>> customizeInfoBuild(@Param("technician") Integer technician, @Param("status") Integer status);
}
