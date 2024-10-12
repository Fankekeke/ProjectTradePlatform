package com.fate.archer.cos.service.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fate.archer.cos.entity.ForeignNotification;
import com.fate.archer.cos.dao.ForeignNotificationMapper;
import com.fate.archer.cos.service.IForeignNotificationService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author FanK
 */
@Service
public class ForeignNotificationServiceImpl extends ServiceImpl<ForeignNotificationMapper, ForeignNotification> implements IForeignNotificationService {

    /**
     * 分页查询外部用户消息通知
     *
     * @param page                分页对象
     * @param foreignNotification 消息通知信息
     * @return 消息列表
     */
    @Override
    public IPage<LinkedHashMap<String, Object>> selectNotificationByPage(Page page, ForeignNotification foreignNotification) {
        return null;
    }

    /**
     * 单用户发送外部用户消息通知
     *
     * @param userCode 用户编号
     * @param content  消息内容
     */
    @Override
    public void sendForeignNotification(String userCode, String title, String content) {
        ForeignNotification notification = new ForeignNotification();
        // 发送用户
        notification.setToUser(userCode);
        // 标题
        notification.setTitle(title);
        // 发送内容
        notification.setContent(content);
        // 发送时间
        notification.setDate(DateUtil.formatDate(new Date()));
        // 消息状态
        notification.setDelFlag(0);
        this.save(notification);
    }

    /**
     * 批量发送外部用户消息通知
     *
     * @param userCodeList 用户编号
     * @param content      消息内容
     */
    @Override
    public void sendForeignNotificationBatch(List<String> userCodeList, String title, String content) {
        List<ForeignNotification> insertData = new ArrayList<>();
        userCodeList.forEach(userCode -> {
            ForeignNotification notification = new ForeignNotification();
            // 发送用户
            notification.setToUser(userCode);
            // 标题
            notification.setTitle(title);
            // 发送内容
            notification.setContent(content);
            // 发送时间
            notification.setDate(DateUtil.formatDate(new Date()));
            // 消息状态
            notification.setDelFlag(0);
        });
        this.saveBatch(insertData);
    }
}
