package com.fate.archer.cos.dao;

import com.fate.archer.cos.entity.OrderInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author FanK
 */
public interface OrderInfoMapper extends BaseMapper<OrderInfo> {

    /**
     * 分页查询订单信息
     *
     * @param page      分页对象
     * @param orderInfo 订单信息
     * @return 订单信息列表
     */
    IPage<OrderInfo> getOrderInfoByPage(Page page, @Param("orderInfo") OrderInfo orderInfo);

    /**
     * 使用兑换码兑换
     *
     * @param code 兑换码
     * @return 订单信息
     */
    OrderInfo getProjectFileByExchangeCode(@Param("code") String code);

    /**
     * 项目详情
     *
     * @param orderCode 订单编号
     * @return 订单详情
     */
    LinkedHashMap<String, Object> orderInfoDetailed(@Param("code") String orderCode);

    /**
     * 用户历史订单
     *
     * @param page     分页对象
     * @param userCode 用户编号
     * @return 用户历史订单
     */
    IPage<OrderInfo> orderHistoryByUserCodePage(Page page, @Param("userCode") String userCode);

    /**
     * 系统主页订单统计
     *
     * @param startDate 开始时间
     * @param endDate   结束时间
     * @param type      类型
     * @return 系统主页订单统计
     */
    List<LinkedHashMap<String, Object>> orderStatistics(@Param("startDate") String startDate, @Param("endDate") String endDate, @Param("type") Integer type);

    /**
     * 近期非定制收益统计
     *
     * @return 近期非定制收益统计
     */
    List<LinkedHashMap<String, Object>> nonCustomizedRevenue();

    /**
     * 月统计平台非定制收益
     *
     * @param teamUser 账户Id
     * @param date     统计时间
     * @return 非定制收益
     */
    BigDecimal incomeMonth(@Param("teamUser") Long teamUser, @Param("date") String date);

    /**
     * 月统计平台定制收益
     *
     * @param date 统计时间
     * @return 定制收益
     */
    BigDecimal customIncomeMonth(@Param("date") String date);

    /**
     * 统计平台总（非定制收益，非定制交易量，定制收益，定制交易量）
     *
     * @return 统计平台总（非定制收益，非定制交易量，定制收益，定制交易量）
     */
    LinkedHashMap<String, Object> moreAll();

    /**
     * 交易量（日环比，月环比）
     *
     * @return 交易量（日环比，月环比）
     */
    LinkedHashMap<String, Object> orderNumRate();

    /**
     * 月统计平台支出
     *
     * @param date 统计时间
     * @return 月统计平台支出
     */
    BigDecimal expensesMonth(@Param("date") String date);

    /**
     * 进行中的定制项目
     *
     * @param teamUser 系统账户
     * @return 进行中的定制项目
     */
    List<LinkedHashMap<String, Object>> progressTask(@Param("teamUser") Long teamUser);

    /**
     * 进行中的非定制部署项目
     *
     * @param teamUser 系统账户
     * @return 进行中的非定制部署项目
     */
    List<LinkedHashMap<String, Object>> nonProgressTask(@Param("teamUser") Long teamUser);

    /**
     * 根据开发者ID获取年统计
     *
     * @param teamId 开发者Id
     * @param year   统计年份
     * @return - -
     */
    List<LinkedHashMap<String, Object>> teamUserStatisticsYear(@Param("teamId") Integer teamId, @Param("year") Integer year);

    /**
     * 根据开发者ID获取订单信息
     *
     * @param teamId 开发者Id
     * @return 订单信息
     */
    List<LinkedHashMap<String, Object>> orderListByDevelopers(@Param("teamId") Integer teamId);

    /**
     * 根据用户编号分页获取订单信息
     *
     * @param page     分页对象
     * @param userCode 用户编号
     * @return 订单信息
     */
    IPage<LinkedHashMap<String, Object>> orderListByUser(Page page, @Param("userCode") String userCode);

    /**
     * 根据订单编号获取详细信息
     *
     * @param orderCode 订单编号
     * @return 订单详细信息
     */
    LinkedHashMap<String, Object> orderInfoByCode(@Param("orderCode") String orderCode);

    /**
     * 根据订单编号获取部署流程
     *
     * @param orderCode 订单编号
     * @return 部署流程
     */
    List<LinkedHashMap<String, Object>> orderDeployProcess(@Param("orderCode") String orderCode);
}
