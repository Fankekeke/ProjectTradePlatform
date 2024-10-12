package com.fate.archer.cos.controller;


import cn.hutool.core.util.StrUtil;
import com.fate.archer.common.annotation.Log;
import com.fate.archer.common.utils.R;
import com.fate.archer.cos.entity.AccessInfo;
import com.fate.archer.cos.service.IAccessInfoService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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
@RequestMapping("/cos/access-info")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AccessInfoController {

    private final IAccessInfoService iAccessInfoService;

    /**
     * 分页查询
     *
     * @param page       分页对象
     * @param accessInfo 访问信息
     * @return 结果
     */
    @Log("获取访问记录")
    @GetMapping("/page")
    public R getBookInfoByPage(Page<AccessInfo> page, AccessInfo accessInfo) {
        return R.ok(iAccessInfoService.page(page, Wrappers.<AccessInfo>lambdaQuery()
                .gt(StrUtil.isNotEmpty(accessInfo.getStartDate()), AccessInfo::getAccessTime, accessInfo.getStartDate())
                .lt(StrUtil.isNotEmpty(accessInfo.getEndDate()), AccessInfo::getAccessTime, accessInfo.getEndDate())
                .like(StrUtil.isNotEmpty(accessInfo.getAccessArea()), AccessInfo::getAccessArea, accessInfo.getAccessArea())
                .orderByDesc(AccessInfo::getAccessTime)));
    }

    /**
     * 新增访问记录
     *
     * @param accessInfo 访问信息
     * @return 保存结果
     */
    @Log("添加访问记录")
    @PostMapping
    public R save(AccessInfo accessInfo) {
        return R.ok(iAccessInfoService.save(accessInfo));
    }


}
