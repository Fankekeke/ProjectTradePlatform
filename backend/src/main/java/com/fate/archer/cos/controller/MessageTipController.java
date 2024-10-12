package com.fate.archer.cos.controller;


import com.fate.archer.common.annotation.Log;
import com.fate.archer.common.utils.R;
import com.fate.archer.cos.entity.MessageTip;
import com.fate.archer.cos.service.IMessageTipService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author FanK
 */
@RestController
@RequestMapping("/cos/message-tip")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MessageTipController {

    private final IMessageTipService messageTipService;

    /**
     * 分页查询消息通知信息
     *
     * @param page       分页对象
     * @param messageTip 消息通知信息
     * @return 消息通知列表
     */
    @Log("查询消息通知信息")
    @GetMapping("/page")
    public R page(Page<MessageTip> page, MessageTip messageTip) {
        return R.ok(messageTipService.getMessageByPage(page, messageTip));
    }

    /**
     * 新增消息通知信息
     *
     * @param messageTip 消息通知信息
     * @return 返回结果
     */
    @Log("新增消息通知信息")
    @PostMapping
    public R save(MessageTip messageTip) {
        return R.ok(messageTipService.save(messageTip));
    }
}
