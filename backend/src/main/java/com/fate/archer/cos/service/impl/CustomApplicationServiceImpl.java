package com.fate.archer.cos.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fate.archer.common.annotation.Log;
import com.fate.archer.common.utils.R;
import com.fate.archer.cos.dao.CustomApplicationMapper;
import com.fate.archer.cos.entity.CustomApplication;
import com.fate.archer.cos.service.ICustomApplicationService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.LinkedHashMap;

/**
 * @author FanK
 */
@Service
public class CustomApplicationServiceImpl extends ServiceImpl<CustomApplicationMapper, CustomApplication> implements ICustomApplicationService {

    @Override
    public IPage<LinkedHashMap<String, Object>> customApplicationPage(Page page, CustomApplication customApplication) {
        return baseMapper.customApplicationPage(page, customApplication);
    }
}
