package com.fate.archer.cos.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fate.archer.common.utils.R;
import com.fate.archer.cos.entity.ThesisInfo;
import com.fate.archer.cos.service.IThesisInfoService;
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
@RequestMapping("/cos/thesis-info")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ThesisInfoController {

    private final IThesisInfoService thesisInfoService;

    /**
     * 论文定制分页查询
     *
     * @param page       分页对象
     * @param thesisInfo 定制信息
     * @return 结果
     */
    @GetMapping("/page")
    public R page(Page page, ThesisInfo thesisInfo) {
        return R.ok(thesisInfoService.selectThesisInfoPage(page, thesisInfo));
    }

    /**
     * 添加论文定制信息
     *
     * @param thesisInfo 定制信息
     * @return 结果
     */
    @PostMapping
    public R save(ThesisInfo thesisInfo) {
        return R.ok(thesisInfoService.addThesisInfo(thesisInfo));
    }

}
