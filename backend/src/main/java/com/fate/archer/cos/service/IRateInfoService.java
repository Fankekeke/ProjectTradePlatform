package com.fate.archer.cos.service;

import com.fate.archer.cos.entity.RateInfo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author FanK
 */
public interface IRateInfoService extends IService<RateInfo> {

    /**
     * 比率
     * @return 比率
     */
    LinkedHashMap<String, Object> rateData();
}
