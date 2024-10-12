package com.fate.archer.cos.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fate.archer.cos.entity.MessageTip;
import com.fate.archer.cos.dao.MessageTipMapper;
import com.fate.archer.cos.service.IMessageTipService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;

/**
 * @author FanK
 */
@Service
public class MessageTipServiceImpl extends ServiceImpl<MessageTipMapper, MessageTip> implements IMessageTipService {

    @Override
    public IPage<LinkedHashMap<String, Object>> getMessageByPage(Page page, MessageTip messageTip) {
        return baseMapper.getMessageByPage(page, messageTip);
    }
}
