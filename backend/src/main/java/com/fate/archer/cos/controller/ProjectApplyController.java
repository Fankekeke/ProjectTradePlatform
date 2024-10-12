package com.fate.archer.cos.controller;


import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fate.archer.common.annotation.Log;
import com.fate.archer.common.domain.BusinessConstant;
import com.fate.archer.common.utils.FileDownload;
import com.fate.archer.common.utils.R;
import com.fate.archer.cos.entity.ProjectApply;
import com.fate.archer.cos.service.INotificationService;
import com.fate.archer.cos.service.IProjectApplyService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author FanK
 */
@RestController
@RequestMapping("/cos/project-apply")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ProjectApplyController {

    private final IProjectApplyService projectApplyService;

    private final INotificationService notificationService;

    /**
     * 查询我的项目
     *
     * @param page         分页对象
     * @param projectApply 我的项目
     * @return 我的项目列表
     */
    @Log("查询我的项目")
    @GetMapping("/page")
    public R page(Page<ProjectApply> page, ProjectApply projectApply) {
        return R.ok(projectApplyService.projectApplyPage(page, projectApply));
    }

    /**
     * 上传我的项目
     *
     * @param projectApply 我的项目
     * @return 返回结果
     */
    @Log("上传我的项目")
    @PostMapping
    public R save(ProjectApply projectApply) {
        projectApply.setReleaseTime(DateUtil.formatDateTime(new Date()));
        projectApply.setProStatus(1);
        return R.ok(projectApplyService.save(projectApply));
    }

    /**
     * 项目审核
     *
     * @param id     审核项目Id
     * @param status 审核状态
     * @return 返回结果
     */
    @Log("项目审核")
    @PutMapping("/audit")
    public R updateStatus(@RequestParam("id") Integer id, @RequestParam("status") Integer status, @RequestParam("price") BigDecimal price) {
        // 获取项目作者信息
        ProjectApply projectApply = projectApplyService.getOne(Wrappers.<ProjectApply>lambdaQuery().eq(ProjectApply::getId, id));
        String content = "";
        if (status == 2) {
            // 生成项目
            projectApplyService.buildTheProject(projectApply, price);
            content = "您的项目《" + projectApply.getProName() + "》 已通过管理员审核！😀";
        } else {
            content = "您的项目《" + projectApply.getProName() + "》 未通过管理员审核！😥，请修改后调整";
        }
        notificationService.setNotification("《" + projectApply.getProName() + "》项目审核", content, projectApply.getAuthor(), BusinessConstant.NOTIFICATION);
        return R.ok(projectApplyService.update(Wrappers.<ProjectApply>lambdaUpdate().set(ProjectApply::getProStatus, status).eq(ProjectApply::getId, id)));
    }

    /**
     * 下载我的项目
     *
     * @param fileName 文件名称
     * @param response 文件流
     */
    @Log("下载我的项目")
    @PostMapping("/download")
    public void download(@RequestParam("fileName") String fileName, HttpServletResponse response) {
        FileDownload.download(BusinessConstant.FILEADDRESS + "file/", fileName, response);
    }

    /**
     * 修改我的项目
     *
     * @param projectApply 我的项目
     * @return 返回结果
     */
    @Log("修改我的项目")
    @PutMapping
    public R update(ProjectApply projectApply) {
        return R.ok(projectApplyService.updateById(projectApply));
    }

    /**
     * 删除项目
     *
     * @param ids 我的项目Id
     * @return 返回结果
     */
    @Log("删除项目")
    @DeleteMapping("/{ids}")
    public R deleteByIds(@PathVariable("ids") List<Integer> ids) {
        return R.ok(projectApplyService.removeByIds(ids));
    }
}
