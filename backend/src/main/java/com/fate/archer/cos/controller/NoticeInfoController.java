package com.fate.archer.cos.controller;


import com.fate.archer.common.annotation.Log;
import com.fate.archer.common.utils.R;
import com.fate.archer.cos.entity.NoticeInfo;
import com.fate.archer.cos.service.INoticeInfoService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * @author FanK
 */
@RestController
@RequestMapping("/cos/notice-info")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class NoticeInfoController {

    private final INoticeInfoService iNoticeInfoService;

    /**
     * 分页查询
     *
     * @param page       分页对象
     * @param noticeInfo 公告信息
     * @return 公告信息列表
     */
    @Log("查询公告信息")
    @GetMapping("/page")
    public R getNoticeInfoByPage(Page<NoticeInfo> page, NoticeInfo noticeInfo) {
        return R.ok(iNoticeInfoService.page(page, Wrappers.query(noticeInfo).orderByDesc("send_date")));
    }

    /**
     * 查询公告信息
     *
     * @return 公告信息列表
     */
    @GetMapping("/list")
    public R getNoticeInfoList() {
        return R.ok(iNoticeInfoService.list(Wrappers.<NoticeInfo>lambdaQuery().orderByDesc(NoticeInfo::getSendDate)));
    }

    /**
     * 新增
     *
     * @param noticeInfo 公告信息
     * @return 返回结果
     */
    @Log("新增公告信息")
    @PostMapping
    public R save(NoticeInfo noticeInfo) {
        noticeInfo.setCode(UUID.randomUUID().toString());
        return R.ok(iNoticeInfoService.save(noticeInfo));
    }

    /**
     * 修改
     *
     * @param noticeInfo 公告信息
     * @return 返回结果
     */
    @Log("修改公告信息")
    @PutMapping
    public R update(NoticeInfo noticeInfo) {
        return R.ok(iNoticeInfoService.updateById(noticeInfo));
    }

    /**
     * 批量删除
     *
     * @param ids 公告信息Id
     * @return 返回结果
     */
    @Log("删除公告信息")
    @DeleteMapping("/{ids}")
    public R deleteByIds(@PathVariable("ids") List<Integer> ids) {
        return R.ok(iNoticeInfoService.removeByIds(ids));
    }

    /**
     * 公告详情
     *
     * @param noticeId 公告ID
     */
    @GetMapping("/{noticeId}")
    public R getById(@PathVariable("noticeId") Integer noticeId) {
        NoticeInfo noticeInfo = iNoticeInfoService.getById(noticeId);
        iNoticeInfoService.update(Wrappers.<NoticeInfo>lambdaUpdate().set(NoticeInfo::getAccessNum, noticeInfo.getAccessNum() + 1).eq(NoticeInfo::getId, noticeId));
        return R.ok(noticeInfo);
    }
}
