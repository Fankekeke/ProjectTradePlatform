package com.fate.archer.cos.controller;


import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.fate.archer.common.annotation.Log;
import com.fate.archer.common.utils.R;
import com.fate.archer.cos.entity.TeamInfo;
import com.fate.archer.cos.service.ITeamInfoService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author FanK
 */
@RestController
@RequestMapping("/cos/team-info")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TeamInfoController {

    private final ITeamInfoService teamInfoService;

    /**
     * 查询团队
     *
     * @param page     返回结果
     * @param teamInfo 团队信息
     * @return 团队信息列表
     */
    @Log("查询团队")
    @GetMapping("/page")
    public R page(Page<TeamInfo> page, TeamInfo teamInfo) {
        return R.ok(teamInfoService.getTeamByPage(page, teamInfo));
    }

    /**
     * 更改成员状态
     *
     * @param id     团队信息Id
     * @param status 状态
     * @return 返回结果
     */
    @PutMapping("/teamStatus")
    public R teamStatus(@RequestParam("id") Integer id, @RequestParam("status") Integer status) {
        return R.ok(teamInfoService.update(Wrappers.<TeamInfo>lambdaUpdate().set(TeamInfo::getStatus, status).eq(TeamInfo::getId, id)));
    }

    /**
     * 根据ID获取团队成员信息
     *
     * @param ids 团队信息Id
     * @return 成员信息
     */
    @GetMapping("/getTeamUserByIds")
    public R getTeamUserInfo(@RequestParam("ids") String ids) {
        return R.ok(teamInfoService.getTeamUserInfo(ids));
    }

    /**
     * 查询未绑定团队的账户
     *
     * @return 未绑定团队的账户
     */
    @GetMapping("/unbound")
    public R teamUnbound() {
        return R.ok(teamInfoService.teamUnbound());
    }

    /**
     * 根据用户ID查询用户信息
     *
     * @param userId 用户Id
     * @return 成员信息
     */
    @GetMapping("/user/{userId}")
    public R userInfoById(@PathVariable("userId") Integer userId) {
        return R.ok();
    }

    /**
     * 团队信息
     *
     * @return 团队信息列表
     */
    @GetMapping("/teamList")
    public R teamList(@RequestParam(value = "account", required = false) String account) {
        return R.ok(teamInfoService.teamList(account));
    }

    /**
     * 添加团队人员
     *
     * @param teamInfo 团队信息
     * @return 返回结果
     */
    @Log("添加团队人员")
    @PostMapping
    public R save(TeamInfo teamInfo) {
        return R.ok(teamInfoService.save(teamInfo));
    }

    /**
     * 修改团队人员
     *
     * @param teamInfo 团队信息
     * @return 返回结果
     */
    @Log("修改团队人员")
    @PutMapping
    public R edit(TeamInfo teamInfo) {
        return R.ok(teamInfoService.updateById(teamInfo));
    }

    /**
     * 删除团队人员
     *
     * @param ids 团队信息Id
     * @return 返回结果
     */
    @Log("删除团队人员")
    @DeleteMapping("/{ids}")
    public R delete(@PathVariable("ids") List<Integer> ids) {
        return R.ok(teamInfoService.removeByIds(ids));
    }

}
