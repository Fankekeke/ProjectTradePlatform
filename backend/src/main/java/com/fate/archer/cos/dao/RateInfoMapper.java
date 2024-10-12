package com.fate.archer.cos.dao;

import com.fate.archer.cos.entity.RateInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author FanK
 */
public interface RateInfoMapper extends BaseMapper<RateInfo> {

    List<LinkedHashMap<String, Object>> customNot();

    List<LinkedHashMap<String, Object>> customNotInvite();

    List<LinkedHashMap<String, Object>> custom();

    List<LinkedHashMap<String, Object>> customInvite();
}
