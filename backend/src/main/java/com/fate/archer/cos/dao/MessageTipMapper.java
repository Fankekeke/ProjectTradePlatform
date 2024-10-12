package com.fate.archer.cos.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fate.archer.cos.entity.MessageTip;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;

/**
 * @author FanK
 */
public interface MessageTipMapper extends BaseMapper<MessageTip> {

    /**
     * 分页获取消息提示数据
     *
     * @param page       分页对象
     * @param messageTip 消息提示
     * @return 消息提示列表
     */
    IPage<LinkedHashMap<String, Object>> getMessageByPage(Page page, @Param("messageTip") MessageTip messageTip);
}
