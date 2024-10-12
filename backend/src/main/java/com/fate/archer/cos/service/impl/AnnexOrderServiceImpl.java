package com.fate.archer.cos.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fate.archer.common.domain.BusinessConstant;
import com.fate.archer.common.exception.FebsException;
import com.fate.archer.common.utils.FileDownload;
import com.fate.archer.cos.dao.AnnexInfoMapper;
import com.fate.archer.cos.entity.AnnexInfo;
import com.fate.archer.cos.entity.AnnexOrder;
import com.fate.archer.cos.dao.AnnexOrderMapper;
import com.fate.archer.cos.service.IAnnexOrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author FanK
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AnnexOrderServiceImpl extends ServiceImpl<AnnexOrderMapper, AnnexOrder> implements IAnnexOrderService {

    private final AnnexInfoMapper annexInfoMapper;

    /**
     * 分页查询附件订单信息
     *
     * @param page       分页对象
     * @param annexOrder 附件订单
     * @return 附件订单列表
     */
    @Override
    public IPage<LinkedHashMap<String, Object>> annexOrderPage(Page page, AnnexOrder annexOrder) {
        return baseMapper.annexOrderPage(page, annexOrder);
    }

    /**
     * 根据开发者ID获取附件订单
     *
     * @param teamId 开发者Id
     * @return 附件订单
     */
    @Override
    public List<LinkedHashMap<String, Object>> thesisOrderList(Integer teamId) {
        return baseMapper.thesisOrderList(teamId);
    }

    /**
     * 根据订单编号获取详细信息
     *
     * @param code 订单编号
     * @return 订单详细信息
     */
    @Override
    public LinkedHashMap<String, Object> annexOrderInfoByCode(String code) {
        return baseMapper.annexOrderInfoByCode(code);
    }

    /**
     * 附件订单下载
     *
     * @param orderCode 附件订单编号
     * @param userCode  用户编号
     * @param response  response
     * @throws Exception 异常
     */
    @Override
    public void download(String orderCode, String userCode, HttpServletResponse response) throws Exception {
        // 获取附件订单信息
        AnnexOrder annexOrder = this.getOne(Wrappers.<AnnexOrder>lambdaQuery().eq(AnnexOrder::getCode, orderCode));
        if (annexOrder == null) {
            throw new FebsException("附件订单不存在!");
        }
        int count = this.count(Wrappers.<AnnexOrder>lambdaQuery().eq(AnnexOrder::getCode, orderCode).eq(AnnexOrder::getUserCode, userCode));
        if (count <= 0) {
            throw new FebsException("此用户无订单信息!");
        }

        // 获取附件信息
        AnnexInfo annex = annexInfoMapper.selectOne(Wrappers.<AnnexInfo>lambdaQuery().eq(AnnexInfo::getCode, annexOrder.getAnnexCode()));
        if (annex == null) {
            throw new FebsException("附件信息不存在!");
        }
        FileDownload.download(BusinessConstant.FILEADDRESS + "db/annex/", annex.getAnnexLocal(), response);
    }
}
