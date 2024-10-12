package com.fate.archer.cos.controller;


import cn.hutool.core.util.StrUtil;
import com.fate.archer.common.annotation.Log;
import com.fate.archer.common.utils.FileDownload;
import com.fate.archer.common.utils.R;
import com.fate.archer.cos.entity.OrderInfo;
import com.fate.archer.cos.entity.ProjectInfo;
import com.fate.archer.cos.entity.UserInfo;
import com.fate.archer.cos.service.IDeployProcessService;
import com.fate.archer.cos.service.IOrderInfoService;
import com.fate.archer.cos.service.IProjectInfoService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fate.archer.cos.service.IUserInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author FanK
 */
@RestController
@RequestMapping("/cos/order-info")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class OrderInfoController {

    private final IOrderInfoService iOrderInfoService;

    private final IProjectInfoService iProjectInfoService;

    private final IDeployProcessService deployProcessService;

    private final IUserInfoService userInfoService;

    /**
     * 分页查询
     *
     * @param page      分页对象
     * @param orderInfo 订单信息
     * @return 订单信息列表
     */
    @Log("查询订单")
    @GetMapping("/page")
    public R getOrderInfoByPage(Page page, OrderInfo orderInfo) {
        return R.ok(iOrderInfoService.getOrderInfoByPage(page, orderInfo));
    }

    /**
     * 平台下单
     *
     * @param orderInfo 订单信息
     * @return 返回结果
     */
    @Log("平台内下单")
    @PostMapping("/orderSaveBySystem")
    public R orderSaveBySystem(OrderInfo orderInfo) {
        // 订单编号
        orderInfo.setCode("ORD-" + System.currentTimeMillis());
        // 订单类型
        orderInfo.setPaymentType(3);
        orderInfo.setOrderStatus(1);
        // 更新项目成交量
        iProjectInfoService.projectTurnover(orderInfo.getProCode());
        // 添加邀请人编号
        if (!StrUtil.isBlank(orderInfo.getUserCode())) {
            UserInfo userInfo = userInfoService.getOne(Wrappers.<UserInfo>lambdaQuery().eq(UserInfo::getCode, orderInfo.getUserCode()));
            orderInfo.setAgent(userInfo.getCode());
        }
        // 生成兑换码,兑换截至日期
        orderInfo.setExchangeCode("EXC-" + System.currentTimeMillis());
        SimpleDateFormat format0 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        orderInfo.setOrderTime(format0.format(new Date()));
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.DATE, 3);
        orderInfo.setEffectiveTime(format0.format(cal.getTime()));
        deployProcessService.deployProcessSave(orderInfo.getCode());
        iOrderInfoService.save(orderInfo);
        return R.ok(orderInfo);
    }

    /**
     * 门户端用户下单
     *
     * @param orderInfo 订单信息
     * @return 返回结果
     */
    @PostMapping("/place/order")
    public R placeOrderFinished(OrderInfo orderInfo) throws Exception {
        return R.ok(iOrderInfoService.placeOrderFinished(orderInfo));
    }

    /**
     * 订单详情
     *
     * @param orderCode 订单编号
     * @return 订单详情
     */
    @Log("订单详情")
    @GetMapping("/detailed/{orderCode}")
    public R orderInfoDetailed(@PathVariable("orderCode") String orderCode) {
        return R.ok(iOrderInfoService.orderInfoDetailed(orderCode));
    }

    /**
     * 项目成交量更新
     *
     * @param proCode 项目编号
     */
    public void projectTurnover(String proCode) {
        ProjectInfo projectInfo = iProjectInfoService.getOne(Wrappers.<ProjectInfo>lambdaQuery().eq(ProjectInfo::getCode, proCode));
        if (projectInfo != null) {
            iProjectInfoService.update(Wrappers.<ProjectInfo>lambdaUpdate().set(ProjectInfo::getTurnover, projectInfo.getTurnover() + 1).eq(ProjectInfo::getId, projectInfo.getId()));
        }
    }

    /**
     * 新增订单
     *
     * @param orderInfo 订单信息
     * @return 返回结果
     */
    @PostMapping
    public R save(OrderInfo orderInfo) {
        orderInfo.setCode("ORD-" + System.currentTimeMillis());
        return R.ok(iOrderInfoService.save(orderInfo));
    }

    /**
     * 修改
     *
     * @param orderInfo 订单信息
     * @return 返回结果
     */
    @Log("修改订单")
    @PutMapping
    public R update(OrderInfo orderInfo) {
        return R.ok(iOrderInfoService.updateById(orderInfo));
    }

    /**
     * 批量删除
     *
     * @param ids 订单Id
     * @return 返回结果
     */
    @Log("删除订单")
    @DeleteMapping("/{ids}")
    public R deleteByIds(@PathVariable("ids") List<Integer> ids) {
        return R.ok(iOrderInfoService.removeByIds(ids));
    }

    /**
     * 下载成品项目
     *
     * @param orderCode 订单编号
     * @param userCode  用户编号
     * @param response  response
     * @throws Exception 异常
     */
    @PostMapping("/download")
    public void download(String orderCode, String userCode, HttpServletResponse response) throws Exception {
        iOrderInfoService.download(orderCode, userCode, response);
    }

}
