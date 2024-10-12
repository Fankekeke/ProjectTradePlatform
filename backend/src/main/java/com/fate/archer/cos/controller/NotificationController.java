package com.fate.archer.cos.controller;


import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fate.archer.common.annotation.Log;
import com.fate.archer.common.utils.R;
import com.fate.archer.cos.entity.NoticeInfo;
import com.fate.archer.cos.entity.Notification;
import com.fate.archer.cos.service.INoticeInfoService;
import com.fate.archer.cos.service.INotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author FanK
 */
@RestController
@RequestMapping("/cos/notification")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class NotificationController {

    private final INotificationService notificationService;

    /**
     * 查询消息通知
     *
     * @param page         分页对象
     * @param notification 消息通知
     * @return 消息通知列表
     */
    @Log("查询消息通知")
    @GetMapping("/page")
    public R page(Page page, Notification notification) {
        return R.ok(notificationService.page(page, notification));
    }

    /**
     * 根据查询统计消息
     *
     * @param userId 用户Id
     * @return 用户消息类型
     */
    @GetMapping("/notificationByType")
    public R notificationByType(@RequestParam("userId") Integer userId) {
        return R.ok(notificationService.notificationByType(userId));
    }

    /**
     * 添加消息通知
     *
     * @param notification 消息通知
     * @return 返回结果
     */
    @Log("添加消息通知")
    @PostMapping
    public R save(Notification notification) {
        return R.ok(notificationService.save(notification));
    }

    /**
     * 消息已阅
     *
     * @param id 消息Id
     * @return 返回结果
     */
    @Log("消息已阅")
    @PutMapping("/haveRead")
    public R delete(@RequestParam("id") Integer id) {
        return R.ok(notificationService.update(Wrappers.<Notification>lambdaUpdate().set(Notification::getDelFlag, 1).eq(Notification::getId, id)));
    }

    /**
     * 一键消息已阅
     *
     * @param ids 消息Id
     * @return 返回结果
     */
    @Log("一键消息已阅")
    @DeleteMapping("/haveRead/{ids}")
    public R deleteByIds(@PathVariable("ids") List<Integer> ids) {
        return R.ok(notificationService.update(Wrappers.<Notification>lambdaUpdate().set(Notification::getDelFlag, 1).in(Notification::getId, ids)));
    }
}
