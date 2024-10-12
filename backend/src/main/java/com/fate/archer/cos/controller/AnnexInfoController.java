package com.fate.archer.cos.controller;


import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.fate.archer.common.annotation.Log;
import com.fate.archer.common.utils.R;
import com.fate.archer.cos.entity.AnnexInfo;
import com.fate.archer.cos.service.IAnnexInfoService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Date;
import java.util.List;

/**
 * @author FanK
 */
@RestController
@RequestMapping("/cos/annex-info")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AnnexInfoController {

    private final IAnnexInfoService annexInfoService;

    /**
     * 分页查询附件信息
     *
     * @param page      分页对象
     * @param annexInfo 附件信息
     * @return 附件信息列表
     */
    @Log("分页查询附件信息")
    @GetMapping("/page")
    public R page(Page<AnnexInfo> page, AnnexInfo annexInfo) {
        return R.ok(annexInfoService.page(page, annexInfo));
    }

    /**
     * 添加附件信息
     *
     * @param annexInfo 附件信息
     * @return 返回结果
     */
    @Log("添加附件信息")
    @PostMapping
    public R save(AnnexInfo annexInfo) {
        return R.ok(annexInfoService.annexSave(annexInfo));
    }

    /**
     * 修改附件信息
     *
     * @param annexInfo 附件信息
     * @return 返回结果
     */
    @Log("修改附件信息")
    @PutMapping
    public R edit(AnnexInfo annexInfo) {
        return R.ok(annexInfoService.updateById(annexInfo));
    }

    /**
     * 删除附件信息
     *
     * @param ids 附件Id
     * @return 返回结果
     */
    @Log("删除附件信息")
    @DeleteMapping("/{ids}")
    public R delete(@PathVariable("ids") List<Integer> ids) {
        return R.ok(annexInfoService.removeByIds(ids));
    }

    /**
     * 下载附件
     *
     * @param fileName 附件名称
     * @param response 文件流
     */
    @Log("下载附件")
    @PostMapping("/download")
    public void download(@RequestParam("fileName") String fileName, HttpServletResponse response) {
        annexInfoService.download(fileName, response);
    }

    /**
     * 附件上下架
     *
     * @param annexId 附件信息Id
     * @param flag    上下架标识
     * @return 返回结果
     */
    @Log("附件上下架")
    @PutMapping("/putOn")
    public R putOn(@RequestParam("id") Integer annexId, @RequestParam("flag") Integer flag) {
        return R.ok(annexInfoService.update(Wrappers.<AnnexInfo>lambdaUpdate().set(AnnexInfo::getIsEnable, flag).eq(AnnexInfo::getId, annexId)));
    }

    /**
     * 附件审核
     *
     * @param id     附件信息Id
     * @param status 状态
     * @return 返回结果
     */
    @Log("附件审核")
    @PutMapping("/audit")
    public R updateStatus(@RequestParam("id") Integer id, @RequestParam("status") Integer status) {
        return R.ok(annexInfoService.updateStatus(id, status));
    }

    /**
     * 门户端附件查询
     *
     * @param page     分页对象
     * @param sortType 排序方式
     * @param key      关键字
     * @return 附件列表
     */
    @GetMapping("/list/page")
    public R selectAnnexPage(Page<AnnexInfo> page, Integer sortType, String key) {
        return R.ok(annexInfoService.selectAnnexPage(page, sortType, key));
    }

    /**
     * 附件详情
     *
     * @param annexCode 附件编号
     * @return 结果
     */
    @GetMapping("/detail/{annexCode}")
    public R selectAnnexDetail(@PathVariable("annexCode") String annexCode) throws Exception {
        return R.ok(annexInfoService.selectAnnexDetail(annexCode));
    }

    /**
     * 根据用户学校信息获取附件交易情况
     *
     * @param userCode  用户编号
     * @param annexCode 附件编号
     * @return 附件交易情况
     * @throws Exception 异常
     */
    @GetMapping("/trade/school")
    public R selectAnnexTradeByUser(String userCode, String annexCode) throws Exception {
        return R.ok(annexInfoService.selectAnnexTradeByUser(userCode, annexCode));
    }
}
