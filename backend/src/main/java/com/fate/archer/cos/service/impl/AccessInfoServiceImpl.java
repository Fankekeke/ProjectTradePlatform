package com.fate.archer.cos.service.impl;

import com.fate.archer.cos.entity.AccessInfo;
import com.fate.archer.cos.dao.AccessInfoMapper;
import com.fate.archer.cos.service.IAccessInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @author FanK
 */
@Service
public class AccessInfoServiceImpl extends ServiceImpl<AccessInfoMapper, AccessInfo> implements IAccessInfoService {

}
