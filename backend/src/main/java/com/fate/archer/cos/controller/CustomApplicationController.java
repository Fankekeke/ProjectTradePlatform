package com.fate.archer.cos.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fate.archer.common.annotation.Log;
import com.fate.archer.common.utils.R;
import com.fate.archer.cos.entity.CustomApplication;
import com.fate.archer.cos.entity.CustomizeInfo;
import com.fate.archer.cos.service.ICustomApplicationService;
import com.fate.archer.cos.service.ICustomizeInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * @author FanK
 */
@RestController
@RequestMapping("/cos/custom-application")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CustomApplicationController {

    private final ICustomApplicationService customApplicationService;

    private final ICustomizeInfoService customizeInfoService;

    /**
     * 定制申请分页查询
     *
     * @param page              分页对象
     * @param customApplication 定制申请
     * @return 定制申请列表
     */
    @Log("定制申请分页查询")
    @GetMapping("/page")
    public R customApplicationPage(Page<CustomApplication> page, CustomApplication customApplication) {
        return R.ok(customApplicationService.customApplicationPage(page, customApplication));
    }

    /**
     * 新增定制申请
     *
     * @param customApplication 定制申请信息
     * @return 返回结果
     */
    @Log("/新增定制申请")
    @PostMapping
    public R save(CustomApplication customApplication) {
        return R.ok(customApplicationService.save(customApplication));
    }

    /**
     * 定制申请生成
     *
     * @param customApplication 定制申请信息
     * @return 返回结果
     */
    @Log("定制申请生成")
    @PostMapping("/made")
    public R applicationMade(CustomApplication customApplication) {
        return R.ok(customizeInfoService.applicationMade(customApplication));
    }

    /**
     * 删除定制申请
     *
     * @param ids 定制申请Id
     * @return 返回结果
     */
    @Log("删除定制申请")
    @DeleteMapping("/{ids}")
    public R delete(@PathVariable("ids") List<Integer> ids) {
        return R.ok(customApplicationService.removeByIds(ids));
    }
}
