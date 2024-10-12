package com.fate.archer.cos.service;

import com.fate.archer.cos.entity.vo.ProductDetailVo;
import com.fate.archer.cos.entity.vo.ProductTradeWithSchoolVo;
import com.fate.archer.cos.entity.ProjectInfo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author FanK
 */
public interface IProjectInfoService extends IService<ProjectInfo> {

    /**
     * 获取Top项目
     *
     * @param key 关键字
     * @return Top项目
     */
    List<LinkedHashMap<String, Object>> getProInfoTop(String key);

    /**
     * 分页查询项目信息
     *
     * @param page    分页对象
     * @param key     关键字
     * @param proType 项目类型
     * @return 项目信息列表
     */
    IPage<LinkedHashMap<String, Object>> getProjectPage(Page page, String key, Integer proType);

    /**
     * 根据ID获取项目信息
     *
     * @param id 项目Id
     * @return 项目信息
     */
    LinkedHashMap<String, Object> getProjectById(Integer id);

    /**
     * 获取全部项目信息（去掉非必要字段）
     *
     * @return 全部项目信息（去掉非必要字段）
     */
    List<LinkedHashMap<String, Object>> getProjectLess();

    /**
     * 根据项目名称模糊查询
     *
     * @param proName 项目名称
     * @return 项目信息
     */
    List<LinkedHashMap<String, Object>> getProFuzzyQuery(String proName);

    /**
     * 用户端项目分页查询
     *
     * @param page     分页对象
     * @param sortType 排序方式
     * @param key      关键字
     * @return 项目列表
     */
    IPage<LinkedHashMap<String, Object>> selectProductList(Page page, Integer sortType, String key);

    /**
     * 项目信息详情
     *
     * @param productCode 项目编号
     * @return 项目信息详情
     * @throws  Exception 异常
     */
    ProductDetailVo productDetail(String productCode) throws Exception;

    /**
     * 根据用户学校信息获取项目交易情况
     *
     * @param userCode    用户编号
     * @param productCode 项目编号
     * @return 交易情况
     * @throws Exception 异常
     */
    List<ProductTradeWithSchoolVo> productTradeByUser(String userCode, String productCode) throws Exception;

    /**
     * 项目成交量更新
     *
     * @param productCode 项目编号
     */
    void projectTurnover(String productCode);
}
