package com.fate.archer.cos.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fate.archer.cos.entity.WealthAssign;
import com.fate.archer.cos.entity.vo.IncomeDetailVo;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author FanK
 */
public interface WealthAssignMapper extends BaseMapper<WealthAssign> {

    /**
     * 根据开发者ID获取非定制项目收益统计
     *
     * @param teamUserId 团队用户ID
     * @return 结果
     */
    List<LinkedHashMap<String, Object>> nonCustomized(@Param("teamUserId") Integer teamUserId);

    /**
     * 根据开发者ID获取定制项目收益统计
     *
     * @param teamUserId 团队用户ID
     * @return 结果
     */
    List<LinkedHashMap<String, Object>> customized(@Param("teamUserId") Integer teamUserId);

    /**
     * 根据开发者ID获取定制论文收益
     *
     * @param teamUserId 团队用户ID
     * @return 结果
     */
    List<LinkedHashMap<String, Object>> thesis(@Param("teamUserId") Integer teamUserId);

    /**
     * 查看成品收益记录
     *
     * @param orderCodes 成品订单编号
     * @param role       角色标识
     * @return 结果
     */
    List<IncomeDetailVo> selectFinshedProductWealthPartByInvite(@Param("orderCodes") List<String> orderCodes, @Param("role") String role);

    /**
     * 查看附件收益记录
     *
     * @param orderCodes 附件订单编号
     * @param role       角色标识
     * @return 结果
     */
    List<IncomeDetailVo> selectAnnexWealthPartByInvite(@Param("orderCodes") List<String> orderCodes, @Param("role") String role);

    /**
     * 查看定制收益记录
     *
     * @param orderCodes 定制订单编号
     * @param role       角色标识
     * @return 结果
     */
    List<IncomeDetailVo> selectCustomWealthList(@Param("orderCodes") List<String> orderCodes, @Param("role") String role);
}
