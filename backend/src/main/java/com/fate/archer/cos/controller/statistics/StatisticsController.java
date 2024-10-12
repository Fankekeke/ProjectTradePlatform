package com.fate.archer.cos.controller.statistics;

import com.fate.archer.common.utils.R;
import com.fate.archer.cos.service.IOrderInfoService;
import com.fate.archer.cos.service.IProjectInfoService;
import com.fate.archer.cos.service.ITakeReviewService;
import com.fate.archer.cos.service.IUserInfoService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/statistics")
@AllArgsConstructor
public class StatisticsController {

    private final IOrderInfoService orderInfoService;

    private final ITakeReviewService takeReviewService;

    private final IUserInfoService userInfoService;

    private final IProjectInfoService projectInfoService;

    /**
     * 系统主页信息
     * @return
     */
    @GetMapping("/home")
    public R homePageData(@RequestParam("roleFlag") Integer roleFlag) {
        return R.ok(orderInfoService.homePageData(roleFlag));
    }

    /**
     * 系统主页订单统计
     * @param startDate
     * @param type
     * @param endDate
     * @return
     */
    @GetMapping("/orderStatistics")
    public R orderStatistics(@RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate, @RequestParam("type") Integer type) {
        return R.ok(orderInfoService.orderStatistics(startDate, endDate, type));
    }
}
