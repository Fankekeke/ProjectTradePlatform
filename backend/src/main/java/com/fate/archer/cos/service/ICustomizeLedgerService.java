package com.fate.archer.cos.service;

import com.fate.archer.cos.entity.CustomizeLedger;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author FanK
 */
public interface ICustomizeLedgerService extends IService<CustomizeLedger> {

    /**
     * 根据定制编号获取定制台账
     *
     * @param code 定制编号
     * @return 定制台账
     */
    LinkedHashMap<String, Object> customLedger(@Param("code") String code);
}
