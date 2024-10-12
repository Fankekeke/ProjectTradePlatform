package com.fate.archer.cos.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fate.archer.cos.entity.ForeignNotification;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author FanK
 */
public interface IForeignNotificationService extends IService<ForeignNotification> {

    /**
     * 分页查询外部用户消息通知
     *
     * @param page                分页对象
     * @param foreignNotification 消息通知信息
     * @return 消息列表
     */
    IPage<LinkedHashMap<String, Object>> selectNotificationByPage(Page page, ForeignNotification foreignNotification);

    /**
     * 单用户发送外部用户消息通知
     *
     * @param userCode 用户编号
     * @param title    标题
     * @param content  消息内容
     */
    void sendForeignNotification(String userCode, String title, String content);

    /**
     * 批量发送外部用户消息通知
     *
     * @param userCodeList 用户编号
     * @param title        标题
     * @param content      消息内容
     */
    void sendForeignNotificationBatch(List<String> userCodeList, String title, String content);
}
