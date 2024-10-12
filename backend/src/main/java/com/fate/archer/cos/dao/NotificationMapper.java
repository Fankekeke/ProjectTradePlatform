package com.fate.archer.cos.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fate.archer.cos.entity.Notification;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author FanK
 */
public interface NotificationMapper extends BaseMapper<Notification> {

    /**
     * 分页查询消息通知信息
     *
     * @param page         分页对象
     * @param notification 消息通知
     * @return 消息通知列表
     */
    IPage<LinkedHashMap<String, Object>> page(Page page, @Param("notification") Notification notification);
}
