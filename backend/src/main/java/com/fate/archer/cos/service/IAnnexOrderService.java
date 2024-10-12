package com.fate.archer.cos.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fate.archer.cos.entity.AnnexOrder;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

import javax.servlet.http.HttpServletResponse;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author FanK
 */
public interface IAnnexOrderService extends IService<AnnexOrder> {

    /**
     * 分页查询附件订单信息
     *
     * @param page       分页对象
     * @param annexOrder 附件订单
     * @return 附件订单列表
     */
    IPage<LinkedHashMap<String, Object>> annexOrderPage(Page page, AnnexOrder annexOrder);

    /**
     * 根据开发者ID获取附件订单
     *
     * @param teamId 开发者Id
     * @return 附件订单
     */
    List<LinkedHashMap<String, Object>> thesisOrderList(Integer teamId);

    /**
     * 根据订单编号获取详细信息
     *
     * @param code 订单编号
     * @return 订单详细信息
     */
    LinkedHashMap<String, Object> annexOrderInfoByCode(String code);

    /**
     * 附件订单下载
     *
     * @param orderCode 附件订单编号
     * @param userCode  用户编号
     * @param response  response
     * @throws Exception 异常
     */
    void download(String orderCode, String userCode, HttpServletResponse response) throws Exception;

}
