package com.fate.archer.cos.controller;


import com.fate.archer.common.annotation.Log;
import com.fate.archer.common.utils.R;
import com.fate.archer.cos.entity.ProjectInfo;
import com.fate.archer.cos.service.IProjectInfoService;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * @author FanK
 */
@RestController
@RequestMapping("/cos/project-info")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ProjectInfoController {

    private final IProjectInfoService iProjectInfoService;

    /**
     * 分页查询
     *
     * @param page        分页对象
     * @param projectInfo 项目信息
     * @return 项目信息列表
     */
    @Log("查询项目信息")
    @GetMapping("/page")
    public R getOrderInfoByPage(Page page, ProjectInfo projectInfo) {
        return R.ok(iProjectInfoService.page(page, Wrappers.<ProjectInfo>lambdaQuery()
                .like(projectInfo.getProName() != null, ProjectInfo::getProName, projectInfo.getProName())
                .like(projectInfo.getProTechnology() != null, ProjectInfo::getProTechnology, projectInfo.getProTechnology())));
    }

    /**
     * 获取Top项目
     *
     * @return Top项目
     */
    @GetMapping("/top")
    public R productTop() {
        return R.ok(iProjectInfoService.getProInfoTop(null));
    }

    /**
     * 项目上下架
     *
     * @param id   项目Id
     * @param type 上下架状态
     * @return 返回结果
     */
    @Log("更改项目上下架状态")
    @PutMapping("/switch")
    public R offShelf(@RequestParam("id") Integer id, @RequestParam("type") Integer type) {
        return R.ok(iProjectInfoService.update(Wrappers.<ProjectInfo>lambdaUpdate().set(ProjectInfo::getDelFlag, type).eq(ProjectInfo::getId, id)));
    }

    /**
     * 根据项目名称模糊查询
     *
     * @param proName 项目名称
     * @return 项目列表
     */
    @GetMapping("/fuzzy/{proName}")
    public R getProFuzzyQuery(@PathVariable(value = "proName", required = false) String proName) {
        return R.ok(iProjectInfoService.getProFuzzyQuery(proName));
    }

    /**
     * 获取全部项目信息
     *
     * @return 全部项目信息
     */
    @Log("查询全部项目信息")
    @GetMapping("/list")
    public R getProjectInfoByList() {
        return R.ok(iProjectInfoService.list(Wrappers.<ProjectInfo>lambdaQuery().eq(ProjectInfo::getDelFlag, 0)));
    }

    /**
     * 获取全部项目信息（去掉非必要字段）
     *
     * @return 全部项目信息（去掉非必要字段）
     */
    @GetMapping("/list/less")
    public R getProjectLess() {
        return R.ok(iProjectInfoService.getProjectLess());
    }

    /**
     * 新增
     *
     * @param projectInfo 项目信息
     * @return 返回结果
     */
    @Log("新增项目")
    @PostMapping
    public R save(ProjectInfo projectInfo) {
        projectInfo.setCode("PRO-" + System.currentTimeMillis());
        return R.ok(iProjectInfoService.save(projectInfo));
    }

    /**
     * 修改
     *
     * @param projectInfo System.currentTimeMillis()
     * @return 返回结果
     */
    @Log("修改项目")
    @PutMapping
    public R update(ProjectInfo projectInfo) {
        return R.ok(iProjectInfoService.updateById(projectInfo));
    }

    /**
     * 批量删除
     *
     * @param ids 项目Id
     * @return 返回结果
     */
    @Log("删除项目")
    @DeleteMapping("/{ids}")
    public R deleteByIds(@PathVariable("ids") List<Integer> ids) {
        return R.ok(iProjectInfoService.removeByIds(ids));
    }

    /**
     * 用户端项目分页查询
     *
     * @param page     分页对象
     * @param sortType 排序方式
     * @param key      关键字
     * @return 项目列表
     */
    @GetMapping("/list/page")
    public R selectProductList(Page page, @RequestParam(required = false) Integer sortType, @RequestParam(required = false) String key) {
        return R.ok(iProjectInfoService.selectProductList(page, sortType, key));
    }

    /**
     * 项目信息详情
     *
     * @param productCode 项目编号
     * @return 项目信息详情
     */
    @GetMapping("/detail/{productCode}")
    public R productDetail(@PathVariable(value = "productCode") String productCode) throws Exception {
        return R.ok(iProjectInfoService.productDetail(productCode));
    }

    /**
     * 根据用户学校获取项目交易信息
     * @param productCode 项目编号
     * @param userCode 用户编号
     * @return 项目交易信息
     */
    @GetMapping("/trade/school")
    public R selectProductTradeByUser(String productCode, String userCode) throws Exception {
        return R.ok(iProjectInfoService.productTradeByUser(userCode, productCode));
    }

}
