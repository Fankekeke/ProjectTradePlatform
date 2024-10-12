package com.fate.archer.cos.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fate.archer.cos.entity.AnnexOrder;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author FanK
 */
public interface AnnexOrderMapper extends BaseMapper<AnnexOrder> {

    /**
     * 分页查询附件订单
     *
     * @param page       分页对象
     * @param annexOrder 附件订单
     * @return 附件订单列表
     */
    IPage<LinkedHashMap<String, Object>> annexOrderPage(Page page, @Param("annexOrder") AnnexOrder annexOrder);

    /**
     * 根据开发者ID获取附件订单
     *
     * @param teamId 开发者Id
     * @return 附件订单
     */
    List<LinkedHashMap<String, Object>> thesisOrderList(@Param("teamId") Integer teamId);

    /**
     * 根据订单编号获取详细信息
     *
     * @param code 订单编号
     * @return 订单详细信息
     */
    LinkedHashMap<String, Object> annexOrderInfoByCode(@Param("code") String code);
}
