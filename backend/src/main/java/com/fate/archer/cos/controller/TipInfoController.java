package com.fate.archer.cos.controller;


import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fate.archer.common.utils.R;
import com.fate.archer.cos.entity.TipInfo;
import com.fate.archer.cos.service.ITipInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author FanK
 */
@RestController
@RequestMapping("/cos/tip-info")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TipInfoController {

    private final ITipInfoService tipInfoService;


    /**
     * 分页查询消息模板数据
     *
     * @param page    分页对象
     * @param tipInfo 消息模板
     * @return 消息模板列表
     */
    @GetMapping("/page")
    public R page(Page page, TipInfo tipInfo) {
        return R.ok();
    }

    /**
     * 新增消息模板信息
     *
     * @param tipInfo 消息模板
     * @return 结果
     */
    @PostMapping
    public R save(TipInfo tipInfo) {
        tipInfo.setDelFlag(0);
        return R.ok(tipInfoService.save(tipInfo));
    }

    /**
     * 修改消息模板信息
     *
     * @param tipInfo 消息模板
     * @return 结果
     */
    @PutMapping
    public R update(TipInfo tipInfo) {
        return R.ok(tipInfoService.updateById(tipInfo));
    }

    /**
     * 删除消息模板信息
     *
     * @param ids 消息模板ID
     * @return 返回结果
     */
    @DeleteMapping("/{ids}")
    public R deleteByIds(@PathVariable("ids") List<Integer> ids) {
        return R.ok(tipInfoService.update(Wrappers.<TipInfo>lambdaUpdate().set(TipInfo::getDelFlag, 1).in(TipInfo::getId, ids)));
    }
}
