package com.fate.archer.cos.service.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fate.archer.cos.entity.Notification;
import com.fate.archer.cos.dao.NotificationMapper;
import com.fate.archer.cos.service.INotificationService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author FanK
 */
@Service
public class NotificationServiceImpl extends ServiceImpl<NotificationMapper, Notification> implements INotificationService {

    @Override
    public LinkedHashMap<String, Object> notificationByType(Integer userId) {
        return new LinkedHashMap<String, Object>() {
            {
                put("t", list(Wrappers.<Notification>lambdaQuery().eq(Notification::getDelFlag, 0).eq(Notification::getType, 1).eq(Notification::getToUser, userId)));
                put("g", list(Wrappers.<Notification>lambdaQuery().eq(Notification::getDelFlag, 0).eq(Notification::getType, 2).eq(Notification::getToUser, userId)));
                put("r", list(Wrappers.<Notification>lambdaQuery().eq(Notification::getDelFlag, 0).eq(Notification::getType, 3).eq(Notification::getToUser, userId)));
                put("count", count(Wrappers.<Notification>lambdaQuery().eq(Notification::getDelFlag, 0).eq(Notification::getToUser, userId)));
            }
        };
    }

    @Override
    public void setNotification(String title, String content, Integer userId, Integer type) {
        Notification notification = new Notification();
        notification.setContent(content);
        notification.setTitle(title);
        notification.setDate(DateUtil.formatDateTime(new Date()));
        notification.setDelFlag(0);
        notification.setToUser(userId);
        notification.setType(type);
        this.save(notification);
    }

    @Override
    public IPage<LinkedHashMap<String, Object>> page(Page page, Notification notification) {
        return baseMapper.page(page, notification);
    }
}
