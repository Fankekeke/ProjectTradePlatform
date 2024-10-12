package com.fate.archer.cos.controller;


import com.fate.archer.common.annotation.Log;
import com.fate.archer.common.utils.R;
import com.fate.archer.cos.entity.MessInfo;
import com.fate.archer.cos.service.IMessInfoService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author FanK
 */
@RestController
@RequestMapping("/cos/mess-info")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MessInfoController {

    private final IMessInfoService iMessInfoService;

    /**
     * 分页查询
     *
     * @param page     分页对象
     * @param messInfo 消息箱
     * @return 消息箱列表
     */
    @Log("查询消息箱")
    @GetMapping("/page")
    public R getMessInfoByPage(Page<MessInfo> page, MessInfo messInfo) {
        return R.ok(iMessInfoService.page(page, Wrappers.query(messInfo).orderByDesc("mess_time")));
    }

    /**
     * 新增
     *
     * @param messInfo 消息信息
     * @return 返回结果
     */
    @Log("新增消息箱信息")
    @PostMapping
    public R save(MessInfo messInfo) {
        return R.ok(iMessInfoService.save(messInfo));
    }

    /**
     * 修改
     *
     * @param messInfo 消息信息
     * @return 返回结果
     */
    @Log("修改消息箱信息")
    @PutMapping
    public R update(MessInfo messInfo) {
        return R.ok(iMessInfoService.updateById(messInfo));
    }

    /**
     * 批量删除
     *
     * @param ids 消息信息Id
     * @return 返回结果
     */
    @Log("删除消息箱信息")
    @DeleteMapping("/{ids}")
    public R deleteByIds(@PathVariable("ids") List<Integer> ids) {
        return R.ok(iMessInfoService.removeByIds(ids));
    }

}
