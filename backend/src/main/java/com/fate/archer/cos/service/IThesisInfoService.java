package com.fate.archer.cos.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fate.archer.cos.entity.ThesisInfo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author FanK
 */
public interface IThesisInfoService extends IService<ThesisInfo> {

    /**
     * 论文定制分页查询
     *
     * @param page       分页对象
     * @param thesisInfo 定制信息
     * @return 结果
     */
    IPage<ThesisInfo> selectThesisInfoPage(Page page, ThesisInfo thesisInfo);

    /**
     * 添加论文定制信息
     *
     * @param thesisInfo 定制信息
     * @return 结果
     */
    Boolean addThesisInfo(ThesisInfo thesisInfo);

    /**
     * 指定论文分配人
     *
     * @param thesisCode 定制论文编号
     * @param technician 分配人
     * @return 结果
     */
    Boolean assigneeTechnician(String thesisCode, Long technician);

}
