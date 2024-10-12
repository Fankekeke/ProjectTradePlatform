package com.fate.archer.cos.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fate.archer.common.annotation.Log;
import com.fate.archer.common.utils.R;
import com.fate.archer.cos.entity.CustomizePaymentProcess;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * @author FanK
 */
@RestController
@RequestMapping("/cos/customize-payment-process")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CustomizePaymentProcessController {

    /**
     * 分页查询定制流程
     * @param page
     * @param customizePaymentProcess
     * @return
     */
    @Log("分页查询定制流程")
    @GetMapping("/page")
    public R page (Page page, CustomizePaymentProcess customizePaymentProcess) {
        return R.ok();
    }
}
