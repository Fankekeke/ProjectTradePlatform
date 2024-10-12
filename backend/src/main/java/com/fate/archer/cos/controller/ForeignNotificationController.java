package com.fate.archer.cos.controller;


import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fate.archer.common.utils.R;
import com.fate.archer.cos.entity.ForeignNotification;
import com.fate.archer.cos.service.IForeignNotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author FanK
 */
@RestController
@RequestMapping("/cos/foreign-notification")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ForeignNotificationController {

    private final IForeignNotificationService foreignNotificationService;

    /**
     * 根据用户编号查询外部用户消息通知
     *
     * @param userCode 用户编号
     * @return 消息列表
     */
    @GetMapping("/list/{userCode}")
    public R getNoticeInfoByUser(@PathVariable("userCode") String userCode) {
        return R.ok(foreignNotificationService.list(Wrappers.<ForeignNotification>lambdaQuery().eq(ForeignNotification::getToUser, userCode).orderByDesc(ForeignNotification::getDate)));
    }

    /**
     * 分页查询外部用户消息通知
     *
     * @param page                分页对象
     * @param foreignNotification 消息通知信息
     * @return 消息列表
     */
    @GetMapping("/page")
    public R getNoticeInfoByPage(Page page, ForeignNotification foreignNotification) {
        return R.ok();
    }

    /**
     * 修改外部用户消息通知状态
     *
     * @param ids 消息通知ID
     * @return 返回结果
     */
    @PutMapping("/{ids}")
    public R updateStatus(@PathVariable("ids") List<Integer> ids) {
        return R.ok(foreignNotificationService.update(Wrappers.<ForeignNotification>lambdaUpdate().set(ForeignNotification::getDelFlag, 1).in(ForeignNotification::getId, ids)));
    }

    /**
     * 删除外部用户消息通知状态
     *
     * @param ids 消息通知ID
     * @return 返回结果
     */
    @DeleteMapping("/{ids}")
    public R delete(@PathVariable("ids") List<Integer> ids) {
        return R.ok(foreignNotificationService.removeByIds(ids));
    }


}
