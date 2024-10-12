package com.fate.archer.cos.dao;

import com.fate.archer.cos.entity.CustomizeOvertime;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author FanK
 */
public interface CustomizeOvertimeMapper extends BaseMapper<CustomizeOvertime> {

    /**
     * 获取所有定制项目加时申请
     *
     * @return 定制项目加时申请
     */
    List<LinkedHashMap<String, Object>> getOverTimeList();
}
