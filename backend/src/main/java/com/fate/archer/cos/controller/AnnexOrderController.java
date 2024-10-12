package com.fate.archer.cos.controller;


import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fate.archer.common.annotation.Log;
import com.fate.archer.common.utils.R;
import com.fate.archer.cos.entity.AnnexOrder;
import com.fate.archer.cos.service.IAnnexOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;

/**
 * @author FanK
 */
@RestController
@RequestMapping("/cos/annex-order")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AnnexOrderController {

    private final IAnnexOrderService annexOrderService;

    /**
     * 分页查询附件订单信息
     *
     * @param page       分页对象
     * @param annexOrder 附件订单
     * @return 附件订单列表
     */
    @Log("分页查询附件订单信息")
    @GetMapping("/page")
    public R page(Page<AnnexOrder> page, AnnexOrder annexOrder) {
        return R.ok(annexOrderService.annexOrderPage(page, annexOrder));
    }

    /**
     * 添加附件订单信息
     *
     * @param annexOrder 附件订单
     * @return 返回结果
     */
    @Log("添加附件订单信息")
    @PostMapping
    public R save(AnnexOrder annexOrder) {
        annexOrder.setOrderTime(DateUtil.formatDateTime(new Date()));
        return R.ok(annexOrderService.save(annexOrder));
    }

    /**
     * 修改附件订单信息
     *
     * @param annexOrder 附件订单
     * @return 返回结果
     */
    @Log("修改附件订单信息")
    @PutMapping
    public R edit(AnnexOrder annexOrder) {
        return R.ok(annexOrderService.updateById(annexOrder));
    }

    /**
     * 删除附件订单信息
     *
     * @param ids 附件订单Id
     * @return 返回结果
     */
    @Log("删除附件订单信息")
    @DeleteMapping("/{ids}")
    public R delete(@PathVariable("ids") List<Integer> ids) {
        return R.ok(annexOrderService.removeByIds(ids));
    }

    /**
     * 附件订单下载
     *
     * @param orderCode 订单编号
     * @param userCode  用户编号
     * @param response  response
     * @throws Exception 异常
     */
    @PostMapping("/download")
    public void download(String orderCode, String userCode, HttpServletResponse response) throws Exception {
        annexOrderService.download(orderCode, userCode, response);
    }

}
