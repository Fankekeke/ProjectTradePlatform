package com.fate.archer.cos.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fate.archer.cos.entity.CustomApplication;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;

/**
 * @author FanK
 */
public interface ICustomApplicationService extends IService<CustomApplication> {

    /**
     * 定制申请分页查询
     *
     * @param page 分页对象
     * @param customApplication 定制申请
     * @return 定制申请列表
     */
    IPage<LinkedHashMap<String, Object>> customApplicationPage(Page page, CustomApplication customApplication);
}
