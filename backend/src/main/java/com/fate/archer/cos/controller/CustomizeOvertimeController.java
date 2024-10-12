package com.fate.archer.cos.controller;


import com.fate.archer.common.annotation.Log;
import com.fate.archer.common.utils.R;
import com.fate.archer.cos.entity.CustomizeOvertime;
import com.fate.archer.cos.service.ICustomizeOvertimeService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * @author FanK
 */
@RestController
@RequestMapping("/cos/customize-overtime")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CustomizeOvertimeController {

    private final ICustomizeOvertimeService customizeOvertimeService;

    /**
     * 获取所有定制项目加时申请
     *
     * @return 定制项目加时申请列表
     */
    @Log("获取所有定制项目加时申请")
    @GetMapping("/list")
    public R getOverTimeList() {
        return R.ok(customizeOvertimeService.getOverTimeList());
    }

    /**
     * 定制项目申请加时
     *
     * @param customizeOvertime 申请加时信息
     * @return 返回结果
     */
    @Log("定制项目申请加时")
    @PostMapping("/apply")
    public R overTimeApply(CustomizeOvertime customizeOvertime) {
        return R.ok(customizeOvertimeService.overTimeApply(customizeOvertime));
    }

    /**
     * 定制项目加时审核
     *
     * @param customizeOvertime 申请加时信息
     * @return 审核结果
     */
    @Log("定制项目加时审核")
    @PostMapping("/check")
    public R overTimeCheck(CustomizeOvertime customizeOvertime) {
        return R.ok(customizeOvertimeService.overTimeCheck(customizeOvertime));
    }
}
