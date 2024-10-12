package com.fate.archer.cos.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fate.archer.cos.entity.MessageTip;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;

/**
 * @author FanK
 */
public interface IMessageTipService extends IService<MessageTip> {

    /**
     * 分页获取消息提示数据
     * @param page 分页对象
     * @param messageTip
     * @return
     */
    IPage<LinkedHashMap<String, Object>> getMessageByPage(Page page, MessageTip messageTip);
}
