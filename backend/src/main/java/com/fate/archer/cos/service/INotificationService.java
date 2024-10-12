package com.fate.archer.cos.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fate.archer.cos.entity.Notification;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author FanK
 */
public interface INotificationService extends IService<Notification> {

    /**
     * 根据用户Id查询通知消息
     *
     * @param userId 用户Id
     * @return 通知消息
     */
    LinkedHashMap<String, Object> notificationByType(Integer userId);

    /**
     * 添加消息通知
     *
     * @param title   标题
     * @param content 内容
     * @param userId  用户Id
     * @param type    类型
     */
    void setNotification(String title, String content, Integer userId, Integer type);

    /**
     * 分页查询通知信息
     *
     * @param page         分页对象
     * @param notification 通知信息
     * @return 通知信息列表
     */
    IPage<LinkedHashMap<String, Object>> page(Page page, @Param("notification") Notification notification);
}
