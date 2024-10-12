package com.fate.archer.cos.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fate.archer.cos.entity.AnnexInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fate.archer.cos.entity.vo.AnnexDetailVo;
import com.fate.archer.cos.entity.vo.ProductTradeWithSchoolVo;
import org.apache.ibatis.annotations.Param;

import javax.servlet.http.HttpServletResponse;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author FanK
 */
public interface IAnnexInfoService extends IService<AnnexInfo> {

    /**
     * 分页查询附件信息
     *
     * @param page      分页对象
     * @param annexInfo 附件信息
     * @return 附件信息列表
     */
    IPage<LinkedHashMap<String, Object>> page(Page<AnnexInfo> page, AnnexInfo annexInfo);

    /**
     * 添加附件信息
     *
     * @param annexInfo 附件信息
     * @return 返回结果
     */
    Boolean annexSave(AnnexInfo annexInfo);

    /**
     * 下载附件
     *
     * @param fileName 附件名称
     * @param response 文件流
     */
    void download(String fileName, HttpServletResponse response);

    /**
     * 附件审核
     *
     * @param id     附件信息Id
     * @param status 状态
     * @return 返回结果
     */
    Boolean updateStatus(Integer id, Integer status);

    /**
     * 门户端附件查询
     *
     * @param page     分页对象
     * @param sortType 排序方式
     * @param key      关键字
     * @return 附件列表
     */
    IPage<LinkedHashMap<String, Object>> selectAnnexPage(Page<AnnexInfo> page, Integer sortType, String key);

    /**
     * 附件详情
     *
     * @param annexCode 附件编号
     * @return 结果
     * @throws Exception 异常
     */
    AnnexDetailVo selectAnnexDetail(String annexCode) throws Exception;

    /**
     * 根据用户学校信息获取附件交易情况
     *
     * @param userCode  用户编号
     * @param annexCode 附件编号
     * @return 附件交易情况
     * @throws Exception 异常
     */
    List<ProductTradeWithSchoolVo> selectAnnexTradeByUser(String userCode, String annexCode) throws Exception;
}
