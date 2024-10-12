package com.fate.archer.cos.service;

import com.fate.archer.cos.entity.AnnexOrder;
import com.fate.archer.cos.entity.WealthAssign;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fate.archer.cos.entity.vo.IncomeDetailVo;

import java.util.List;
import java.util.Map;

/**
 * @author FanK
 */
public interface IWealthAssignService extends IService<WealthAssign> {

    /**
     * 附件订单佣金分配
     *
     * @param annexOrder 附件订单信息
     * @return 返回结果
     * @throws Exception 异常
     */
    boolean wealthAssignAnnex(AnnexOrder annexOrder) throws Exception;

    /**
     * 审核通过后佣金分配
     *
     * @param wealthAssign 佣金分配
     * @return 返回结果
     */
    boolean wealthAssign(WealthAssign wealthAssign);

    /**
     * 定制审核通过佣金分配
     *
     * @param wealthAssign 佣金分配
     * @return 返回结果
     */

    boolean wealthAssignCustomize(WealthAssign wealthAssign);

    /**
     * 邀请人查看收益记录
     *
     * @param userCode 邀请人编号
     * @return 收益记录
     * @throws Exception 异常
     */
    List<IncomeDetailVo> selectWealthPartByInvite(String userCode) throws Exception;

    /**
     * 开发查看收益记录
     *
     * @param teamId 开发者ID
     * @return 收益记录
     * @throws Exception 异常
     */
    List<IncomeDetailVo> selectWealthPartByDeveloper(Integer teamId) throws Exception;

    /**
     * 平台查看收益记录
     *
     * @return 收益记录
     * @throws Exception 异常
     */
    List<IncomeDetailVo> selectWealthPartByPlatform() throws Exception;

}
