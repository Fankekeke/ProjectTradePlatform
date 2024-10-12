package com.fate.archer.cos.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fate.archer.cos.entity.AnnexInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;

/**
 * @author FanK
 */
public interface AnnexInfoMapper extends BaseMapper<AnnexInfo> {

    /**
     * 分页查询附件信息
     *
     * @param page      分页对象
     * @param annexInfo 附件信息
     * @return 附件信息列表
     */
    IPage<LinkedHashMap<String, Object>> page(Page page, @Param("annexInfo") AnnexInfo annexInfo);

    /**
     * 门户端附件查询
     *
     * @param page     分页对象
     * @param sortType 排序方式
     * @param key      关键字
     * @return 附件列表
     */
    IPage<LinkedHashMap<String, Object>> selectAnnexPage(Page page, @Param("sortType") Integer sortType, @Param("key") String key);
}
