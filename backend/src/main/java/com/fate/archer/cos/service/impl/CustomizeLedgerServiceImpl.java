package com.fate.archer.cos.service.impl;

import com.fate.archer.cos.entity.CustomizeLedger;
import com.fate.archer.cos.dao.CustomizeLedgerMapper;
import com.fate.archer.cos.service.ICustomizeLedgerService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author FanK
 */
@Service
public class CustomizeLedgerServiceImpl extends ServiceImpl<CustomizeLedgerMapper, CustomizeLedger> implements ICustomizeLedgerService {

    @Override
    public LinkedHashMap<String, Object> customLedger(String code) {
        return baseMapper.customLedger(code);
    }
}
