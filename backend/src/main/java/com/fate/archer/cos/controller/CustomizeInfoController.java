package com.fate.archer.cos.controller;


import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.fate.archer.common.annotation.Log;
import com.fate.archer.common.domain.BusinessConstant;
import com.fate.archer.common.utils.R;
import com.fate.archer.cos.entity.*;
import com.fate.archer.cos.service.ICustomizeInfoService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fate.archer.cos.service.IUserInfoService;
import com.fate.archer.cos.service.IWealthAssignService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author FanK
 */
@RestController
@RequestMapping("/cos/customize-info")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CustomizeInfoController {

    public final ICustomizeInfoService customizeInfoService;

    /**
     * 分页查询项目定制信息
     *
     * @param page          分页对象
     * @param customizeInfo 项目定制信息
     * @return 项目定制信息列表
     */
    @Log("分页查询项目定制信息")
    @GetMapping("/page")
    public R page(Page page, CustomizeInfo customizeInfo) {
        return R.ok(customizeInfoService.customizeInfoPage(page, customizeInfo));
    }

    /**
     * 项目定制审核
     *
     * @param orderCode 定制编号
     * @param status    审核状态
     * @return 返回结果
     */
    @Log("项目定制审核")
    @PostMapping("/audit")
    public R customizeAudit(@RequestParam("orderCode") String orderCode, @RequestParam("status") Integer status) {
        return R.ok(customizeInfoService.customizeAudit(orderCode, status));
    }

    /**
     * 定制项目开发者绑定
     *
     * @param ids 开发者Id
     * @return 绑定结果
     */
    @Log("定制项目开发者绑定")
    @PutMapping("/developer/bind")
    public R developerBind(@RequestParam("ids") String ids, @RequestParam("customCode") String customCode) {
        return R.ok(customizeInfoService.assignment(customCode, ids));
    }

    /**
     * 定制项目撤销论文模块
     *
     * @param customCode 定制编号
     * @return 返回结果
     */
    @Log("定制项目撤销论文模块")
    @PostMapping("/revokeThesis")
    public R revokeThesis(@RequestParam("customCode") String customCode) {
        return R.ok(customizeInfoService.revokeThesis(customCode));
    }

    /**
     * 新增项目定制信息
     *
     * @param customizeInfo 项目定制信息
     * @return 返回结果
     */
    @Log("新增项目定制信息")
    @PostMapping
    public R save(CustomizeInfo customizeInfo) {
        return R.ok(customizeInfoService.save(customizeInfo));
    }

    /**
     * 修改项目定制信息
     *
     * @param customizeInfo 项目定制信息
     * @return 返回结果
     */
    @Log("修改项目定制信息")
    @PutMapping
    public R edit(CustomizeInfo customizeInfo) {
        return R.ok(customizeInfoService.updateById(customizeInfo));
    }

    /**
     * 删除项目定制信息
     *
     * @param ids 项目定制Id
     * @return 返回结果
     */
    @Log("删除项目定制信息")
    @DeleteMapping("{/ids}")
    public R delete(@PathVariable("ids") List<Integer> ids) {
        return R.ok(customizeInfoService.removeByIds(ids));
    }
}
