package com.fate.archer.cos.dao;

import com.fate.archer.cos.entity.CustomizeLedger;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author FanK
 */
public interface CustomizeLedgerMapper extends BaseMapper<CustomizeLedger> {

    /**
     * 根据定制编号获取定制台账
     *
     * @param code 定制编号
     * @return 定制台账
     */
    LinkedHashMap<String, Object> customLedger(@Param("code") String code);
}
