package com.fate.archer.common.utils;

import cn.hutool.json.JSONUtil;
import com.fate.archer.common.query.Producer;
import com.fate.archer.cos.dto.query.AccountVerify;
import com.fate.archer.cos.service.IForeignNotificationService;
import com.fate.archer.cos.service.INotificationService;
import lombok.RequiredArgsConstructor;
import org.apache.activemq.command.ActiveMQQueue;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.jms.Destination;
import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MessageUtil {

    private final IForeignNotificationService foreignNotificationService;

    private final INotificationService notificationService;

    private final Producer producer;

    /**
     * 发送门户端消息
     * @param userCodeList 用户编号
     * @param title 消息标题1
     * @param content 消息内容
     */
    public void sendMessage(List<String> userCodeList, String title, String content) {
        if (CollectionUtils.isEmpty(userCodeList)) {
            return;
        }
        foreignNotificationService.sendForeignNotificationBatch(userCodeList, title, content);

        // 发送到队列（短信邮箱）
//        AccountVerify accountVerify = new AccountVerify("用户", phone, 1, random, 2);
//        Destination destination = new ActiveMQQueue("assist.queue");
//        producer.sendMessage(destination, JSONUtil.toJsonStr(accountVerify));
    }
}
