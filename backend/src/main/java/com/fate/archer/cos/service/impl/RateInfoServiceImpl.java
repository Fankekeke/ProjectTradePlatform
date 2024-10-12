package com.fate.archer.cos.service.impl;

import com.fate.archer.cos.entity.RateInfo;
import com.fate.archer.cos.dao.RateInfoMapper;
import com.fate.archer.cos.service.IRateInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author FanK
 */
@Service
public class RateInfoServiceImpl extends ServiceImpl<RateInfoMapper, RateInfo> implements IRateInfoService {

    @Override
    public LinkedHashMap<String, Object> rateData() {
        return new LinkedHashMap<String, Object>() {
            {
                put("custom", baseMapper.custom());
                put("customInvite", baseMapper.customInvite());
                put("customNot", baseMapper.customNot());
                put("customNotInvite", baseMapper.customNotInvite());
            }
        };
    }
}
