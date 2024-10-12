package com.fate.archer.cos.service;

import com.fate.archer.cos.entity.CustomizeOvertime;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author FanK
 */
public interface ICustomizeOvertimeService extends IService<CustomizeOvertime> {

    /**
     * 定制项目申请加时
     *
     * @param customizeOvertime 加时申请
     * @return 返回结果
     */
    Boolean overTimeApply(CustomizeOvertime customizeOvertime);

    /**
     * 定制项目加时审核
     *
     * @param customizeOvertime 定制项目加时
     * @return 返回结果
     */
    Boolean overTimeCheck(CustomizeOvertime customizeOvertime);

    /**
     * 获取所有定制项目加时申请
     *
     * @return 获取所有定制项目加时申请
     */
    List<LinkedHashMap<String, Object>> getOverTimeList();
}
