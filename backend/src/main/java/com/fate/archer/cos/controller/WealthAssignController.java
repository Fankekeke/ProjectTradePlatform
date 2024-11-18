package com.fate.archer.cos.controller;


import com.fate.archer.common.utils.R;
import com.fate.archer.cos.entity.vo.IncomeDetailVo;
import com.fate.archer.cos.service.IWealthAssignService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author FanK
 */
@RestController
@RequestMapping("/cos/wealth-assign")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class WealthAssignController {

    private final IWealthAssignService wealthAssignService;

    /**
     * 邀请人查看收益记录
     *
     * @param userCode 邀请人编号
     * @return 收益记录
     * @throws Exception 异常
     */
    @GetMapping("/wealth/invite/{userCode}")
    public R selectWealthPartByInvite(@PathVariable("userCode") String userCode) throws Exception {
        return R.ok(wealthAssignService.selectWealthPartByInvite(userCode));
    }

    /**
     * 开发查看收益记录
     *
     * @param teamId 开发者ID
     * @return 收益记录
     * @throws Exception 异常
     */
    @GetMapping("/wealth/developer/{teamId}")
    public R selectWealthPartByDeveloper(@PathVariable("teamId") Integer teamId) throws Exception {
        return R.ok(wealthAssignService.selectWealthPartByDeveloper(teamId));
    }

    /**
     * 平台查看收益记录
     *
     * @return 收益记录
     * @throws Exception 异常
     */
    @GetMapping("/wealth/developer")
    public R selectWealthPartByPlatform() throws Exception {
        return R.ok(wealthAssignService.selectWealthPartByPlatform());
    }
}
