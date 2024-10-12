package com.fate.archer.cos.dao;

import com.fate.archer.cos.entity.ProjectInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author FanK
 */
public interface ProjectInfoMapper extends BaseMapper<ProjectInfo> {

    /**
     * 获取Top项目
     *
     * @param key - -
     * @return Top项目
     */
    List<LinkedHashMap<String, Object>> getProInfoTop(@Param("key") String key);

    /**
     * 分页查询项目信息
     *
     * @param page    分页对象
     * @param key     项目分类
     * @param proType 项目类型
     * @return 项目信息
     */
    IPage<LinkedHashMap<String, Object>> getProjectPage(Page page, @Param("key") String key, @Param("proType") Integer proType);

    /**
     * 根据ID获取项目信息
     *
     * @param id - -
     * @return 项目信息
     */
    LinkedHashMap<String, Object> getProjectById(@Param("id") Integer id);

    /**
     * 获取全部项目信息（去掉非必要字段）
     *
     * @return 项目信息
     */
    List<LinkedHashMap<String, Object>> getProjectLess();

    /**
     * 根据项目名称模糊查询
     *
     * @param proName key
     * @return 项目信息
     */
    List<LinkedHashMap<String, Object>> getProFuzzyQuery(@Param("proName") String proName);

    /**
     * 用户端项目分页查询
     *
     * @param page     分页对象
     * @param sortType 排序方式
     * @param key      关键字
     * @return 项目列表
     */
    IPage<LinkedHashMap<String, Object>> selectProductList(Page page, @Param("sortType") Integer sortType, @Param("key") String key);
}
